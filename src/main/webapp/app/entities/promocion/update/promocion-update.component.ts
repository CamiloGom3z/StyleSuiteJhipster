import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPromocion, Promocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-promocion-update',
  templateUrl: './promocion-update.component.html',
})
export class PromocionUpdateComponent implements OnInit {
  isSaving = false;

  serviciosSharedCollection: IServicio[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    porcentajeDescuento: [],
    fechaInicio: [],
    fechaFin: [],
    tipoPromocion: [],
    servicios: [],
  });

  constructor(
    protected promocionService: PromocionService,
    protected servicioService: ServicioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promocion }) => {
      if (promocion.id === undefined) {
        const today = dayjs().startOf('day');
        promocion.fechaInicio = today;
        promocion.fechaFin = today;
      }

      this.updateForm(promocion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promocion = this.createFromForm();
    if (promocion.id !== undefined) {
      this.subscribeToSaveResponse(this.promocionService.update(promocion));
    } else {
      this.subscribeToSaveResponse(this.promocionService.create(promocion));
    }
  }

  trackServicioById(_index: number, item: IServicio): number {
    return item.id!;
  }

  getSelectedServicio(option: IServicio, selectedVals?: IServicio[]): IServicio {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromocion>>): void {
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

  protected updateForm(promocion: IPromocion): void {
    this.editForm.patchValue({
      id: promocion.id,
      nombre: promocion.nombre,
      descripcion: promocion.descripcion,
      porcentajeDescuento: promocion.porcentajeDescuento,
      fechaInicio: promocion.fechaInicio ? promocion.fechaInicio.format(DATE_TIME_FORMAT) : null,
      fechaFin: promocion.fechaFin ? promocion.fechaFin.format(DATE_TIME_FORMAT) : null,
      tipoPromocion: promocion.tipoPromocion,
      servicios: promocion.servicios,
    });

    this.serviciosSharedCollection = this.servicioService.addServicioToCollectionIfMissing(
      this.serviciosSharedCollection,
      ...(promocion.servicios ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing(servicios, ...(this.editForm.get('servicios')!.value ?? []))
        )
      )
      .subscribe((servicios: IServicio[]) => (this.serviciosSharedCollection = servicios));
  }

  protected createFromForm(): IPromocion {
    return {
      ...new Promocion(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      porcentajeDescuento: this.editForm.get(['porcentajeDescuento'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value
        ? dayjs(this.editForm.get(['fechaInicio'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      tipoPromocion: this.editForm.get(['tipoPromocion'])!.value,
      servicios: this.editForm.get(['servicios'])!.value,
    };
  }
}
