import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICita, Cita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { IAgendaEmpleado } from 'app/entities/agenda-empleado/agenda-empleado.model';
import { AgendaEmpleadoService } from 'app/entities/agenda-empleado/service/agenda-empleado.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { EstadoCitaEnum } from 'app/entities/enumerations/estado-cita-enum.model';

@Component({
  selector: 'jhi-cita-update',
  templateUrl: './cita-update.component.html',
})
export class CitaUpdateComponent implements OnInit {
  isSaving = false;
  estadoCitaEnumValues = Object.keys(EstadoCitaEnum);

  agendaEmpleadosCollection: IAgendaEmpleado[] = [];
  personasSharedCollection: IPersona[] = [];

  editForm = this.fb.group({
    id: [],
    fechaCita: [],
    fechaFinCita: [],
    estado: [],
    notas: [],
    agendaEmpleado: [],
    cliente: [],
  });

  constructor(
    protected citaService: CitaService,
    protected agendaEmpleadoService: AgendaEmpleadoService,
    protected personaService: PersonaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cita }) => {
      if (cita.id === undefined) {
        const today = dayjs().startOf('day');
        cita.fechaCita = today;
        cita.fechaFinCita = today;
      }

      this.updateForm(cita);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cita = this.createFromForm();
    if (cita.id !== undefined) {
      this.subscribeToSaveResponse(this.citaService.update(cita));
    } else {
      this.subscribeToSaveResponse(this.citaService.create(cita));
    }
  }

  trackAgendaEmpleadoById(_index: number, item: IAgendaEmpleado): number {
    return item.id!;
  }

  trackPersonaById(_index: number, item: IPersona): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICita>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cita: ICita): void {
    this.editForm.patchValue({
      id: cita.id,
      fechaCita: cita.fechaCita ? cita.fechaCita.format(DATE_TIME_FORMAT) : null,
      fechaFinCita: cita.fechaFinCita ? cita.fechaFinCita.format(DATE_TIME_FORMAT) : null,
      estado: cita.estado,
      notas: cita.notas,
      agendaEmpleado: cita.agendaEmpleado,
      cliente: cita.cliente,
    });

    this.agendaEmpleadosCollection = this.agendaEmpleadoService.addAgendaEmpleadoToCollectionIfMissing(
      this.agendaEmpleadosCollection,
      cita.agendaEmpleado
    );
    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasSharedCollection, cita.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.agendaEmpleadoService
      .query({ filter: 'cita-is-null' })
      .pipe(map((res: HttpResponse<IAgendaEmpleado[]>) => res.body ?? []))
      .pipe(
        map((agendaEmpleados: IAgendaEmpleado[]) =>
          this.agendaEmpleadoService.addAgendaEmpleadoToCollectionIfMissing(agendaEmpleados, this.editForm.get('agendaEmpleado')!.value)
        )
      )
      .subscribe((agendaEmpleados: IAgendaEmpleado[]) => (this.agendaEmpleadosCollection = agendaEmpleados));

    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('cliente')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
  }

  protected createFromForm(): ICita {
    return {
      ...new Cita(),
      id: this.editForm.get(['id'])!.value,
      fechaCita: this.editForm.get(['fechaCita'])!.value ? dayjs(this.editForm.get(['fechaCita'])!.value, DATE_TIME_FORMAT) : undefined,
      fechaFinCita: this.editForm.get(['fechaFinCita'])!.value
        ? dayjs(this.editForm.get(['fechaFinCita'])!.value, DATE_TIME_FORMAT)
        : undefined,
      estado: this.editForm.get(['estado'])!.value,
      notas: this.editForm.get(['notas'])!.value,
      agendaEmpleado: this.editForm.get(['agendaEmpleado'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
    };
  }
}
