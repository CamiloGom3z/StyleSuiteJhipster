import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAgendaEmpleado, AgendaEmpleado } from '../agenda-empleado.model';
import { AgendaEmpleadoService } from '../service/agenda-empleado.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-agenda-empleado-update',
  templateUrl: './agenda-empleado-update.component.html',
})
export class AgendaEmpleadoUpdateComponent implements OnInit {
  isSaving = false;

  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    fechaInicio: [],
    fechaFin: [],
    disponible: [],
    empleado: [],
  });

  constructor(
    protected agendaEmpleadoService: AgendaEmpleadoService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agendaEmpleado }) => {
      if (agendaEmpleado.id === undefined) {
        const today = dayjs().startOf('day');
        agendaEmpleado.fechaInicio = today;
        agendaEmpleado.fechaFin = today;
      }

      this.updateForm(agendaEmpleado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agendaEmpleado = this.createFromForm();
    if (agendaEmpleado.id !== undefined) {
      this.subscribeToSaveResponse(this.agendaEmpleadoService.update(agendaEmpleado));
    } else {
      this.subscribeToSaveResponse(this.agendaEmpleadoService.create(agendaEmpleado));
    }
  }

  trackEmpleadoById(_index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgendaEmpleado>>): void {
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

  protected updateForm(agendaEmpleado: IAgendaEmpleado): void {
    this.editForm.patchValue({
      id: agendaEmpleado.id,
      fechaInicio: agendaEmpleado.fechaInicio ? agendaEmpleado.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: agendaEmpleado.fechaFin ? agendaEmpleado.fechaFin.format(DATE_TIME_FORMAT) : null,
      disponible: agendaEmpleado.disponible,
      empleado: agendaEmpleado.empleado,
    });

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(
      this.empleadosSharedCollection,
      agendaEmpleado.empleado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): IAgendaEmpleado {
    return {
      ...new AgendaEmpleado(),
      id: this.editForm.get(['id'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      disponible: this.editForm.get(['disponible'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
    };
  }
}
