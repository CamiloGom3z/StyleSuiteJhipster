import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IServicio, Servicio } from '../servicio.model';
import { ServicioService } from '../service/servicio.service';
import { ITipoServcio } from 'app/entities/tipo-servcio/tipo-servcio.model';
import { TipoServcioService } from 'app/entities/tipo-servcio/service/tipo-servcio.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';

@Component({
  selector: 'jhi-servicio-update',
  templateUrl: './servicio-update.component.html',
})
export class ServicioUpdateComponent implements OnInit {
  isSaving = false;

  tipoServciosSharedCollection: ITipoServcio[] = [];
  citasSharedCollection: ICita[] = [];

  editForm = this.fb.group({
    id: [],
    valorTotalServicio: [],
    descripcion: [],
    fechaInicio: [],
    fechaFin: [],
    tipoServicio: [],
    cita: [],
  });

  constructor(
    protected servicioService: ServicioService,
    protected tipoServcioService: TipoServcioService,
    protected citaService: CitaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicio }) => {
      if (servicio.id === undefined) {
        const today = dayjs().startOf('day');
        servicio.fechaInicio = today;
        servicio.fechaFin = today;
      }

      this.updateForm(servicio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicio = this.createFromForm();
    if (servicio.id !== undefined) {
      this.subscribeToSaveResponse(this.servicioService.update(servicio));
    } else {
      this.subscribeToSaveResponse(this.servicioService.create(servicio));
    }
  }

  trackTipoServcioById(_index: number, item: ITipoServcio): number {
    return item.id!;
  }

  trackCitaById(_index: number, item: ICita): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicio>>): void {
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

  protected updateForm(servicio: IServicio): void {
    this.editForm.patchValue({
      id: servicio.id,
      valorTotalServicio: servicio.valorTotalServicio,
      descripcion: servicio.descripcion,
      fechaInicio: servicio.fechaInicio ? servicio.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: servicio.fechaFin ? servicio.fechaFin.format(DATE_TIME_FORMAT) : null,
      tipoServicio: servicio.tipoServicio,
      cita: servicio.cita,
    });

    this.tipoServciosSharedCollection = this.tipoServcioService.addTipoServcioToCollectionIfMissing(
      this.tipoServciosSharedCollection,
      servicio.tipoServicio
    );
    this.citasSharedCollection = this.citaService.addCitaToCollectionIfMissing(this.citasSharedCollection, servicio.cita);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoServcioService
      .query()
      .pipe(map((res: HttpResponse<ITipoServcio[]>) => res.body ?? []))
      .pipe(
        map((tipoServcios: ITipoServcio[]) =>
          this.tipoServcioService.addTipoServcioToCollectionIfMissing(tipoServcios, this.editForm.get('tipoServicio')!.value)
        )
      )
      .subscribe((tipoServcios: ITipoServcio[]) => (this.tipoServciosSharedCollection = tipoServcios));

    this.citaService
      .query()
      .pipe(map((res: HttpResponse<ICita[]>) => res.body ?? []))
      .pipe(map((citas: ICita[]) => this.citaService.addCitaToCollectionIfMissing(citas, this.editForm.get('cita')!.value)))
      .subscribe((citas: ICita[]) => (this.citasSharedCollection = citas));
  }

  protected createFromForm(): IServicio {
    return {
      ...new Servicio(),
      id: this.editForm.get(['id'])!.value,
      valorTotalServicio: this.editForm.get(['valorTotalServicio'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      tipoServicio: this.editForm.get(['tipoServicio'])!.value,
      cita: this.editForm.get(['cita'])!.value,
    };
  }
}
