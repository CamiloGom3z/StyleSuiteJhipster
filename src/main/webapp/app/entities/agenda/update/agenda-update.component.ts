import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAgenda, Agenda } from '../agenda.model';
import { AgendaService } from '../service/agenda.service';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

@Component({
  selector: 'jhi-agenda-update',
  templateUrl: './agenda-update.component.html',
})
export class AgendaUpdateComponent implements OnInit {
  isSaving = false;

  establecimientosSharedCollection: IEstablecimiento[] = [];

  editForm = this.fb.group({
    id: [],
    fechaInicio: [],
    fechaFin: [],
    disponible: [],
    establecimiento: [],
  });

  constructor(
    protected agendaService: AgendaService,
    protected establecimientoService: EstablecimientoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agenda }) => {
      if (agenda.id === undefined) {
        const today = dayjs().startOf('day');
        agenda.fechaInicio = today;
        agenda.fechaFin = today;
      }

      this.updateForm(agenda);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agenda = this.createFromForm();
    if (agenda.id !== undefined) {
      this.subscribeToSaveResponse(this.agendaService.update(agenda));
    } else {
      this.subscribeToSaveResponse(this.agendaService.create(agenda));
    }
  }

  trackEstablecimientoById(_index: number, item: IEstablecimiento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgenda>>): void {
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

  protected updateForm(agenda: IAgenda): void {
    this.editForm.patchValue({
      id: agenda.id,
      fechaInicio: agenda.fechaInicio ? agenda.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: agenda.fechaFin ? agenda.fechaFin.format(DATE_TIME_FORMAT) : null,
      disponible: agenda.disponible,
      establecimiento: agenda.establecimiento,
    });

    this.establecimientosSharedCollection = this.establecimientoService.addEstablecimientoToCollectionIfMissing(
      this.establecimientosSharedCollection,
      agenda.establecimiento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.establecimientoService
      .query()
      .pipe(map((res: HttpResponse<IEstablecimiento[]>) => res.body ?? []))
      .pipe(
        map((establecimientos: IEstablecimiento[]) =>
          this.establecimientoService.addEstablecimientoToCollectionIfMissing(establecimientos, this.editForm.get('establecimiento')!.value)
        )
      )
      .subscribe((establecimientos: IEstablecimiento[]) => (this.establecimientosSharedCollection = establecimientos));
  }

  protected createFromForm(): IAgenda {
    return {
      ...new Agenda(),
      id: this.editForm.get(['id'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      disponible: this.editForm.get(['disponible'])!.value,
      establecimiento: this.editForm.get(['establecimiento'])!.value,
    };
  }
}
