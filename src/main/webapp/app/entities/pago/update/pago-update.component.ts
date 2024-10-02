import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPago, Pago } from '../pago.model';
import { PagoService } from '../service/pago.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';
import { MetodoPagoEnum } from 'app/entities/enumerations/metodo-pago-enum.model';

@Component({
  selector: 'jhi-pago-update',
  templateUrl: './pago-update.component.html',
})
export class PagoUpdateComponent implements OnInit {
  isSaving = false;
  metodoPagoEnumValues = Object.keys(MetodoPagoEnum);

  citasSharedCollection: ICita[] = [];

  editForm = this.fb.group({
    id: [],
    monto: [],
    fechaPago: [],
    metodoPago: [],
    estado: [],
    cita: [],
  });

  constructor(
    protected pagoService: PagoService,
    protected citaService: CitaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pago }) => {
      if (pago.id === undefined) {
        const today = dayjs().startOf('day');
        pago.fechaPago = today;
      }

      this.updateForm(pago);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pago = this.createFromForm();
    if (pago.id !== undefined) {
      this.subscribeToSaveResponse(this.pagoService.update(pago));
    } else {
      this.subscribeToSaveResponse(this.pagoService.create(pago));
    }
  }

  trackCitaById(_index: number, item: ICita): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPago>>): void {
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

  protected updateForm(pago: IPago): void {
    this.editForm.patchValue({
      id: pago.id,
      monto: pago.monto,
      fechaPago: pago.fechaPago ? pago.fechaPago.format(DATE_TIME_FORMAT) : null,
      metodoPago: pago.metodoPago,
      estado: pago.estado,
      cita: pago.cita,
    });

    this.citasSharedCollection = this.citaService.addCitaToCollectionIfMissing(this.citasSharedCollection, pago.cita);
  }

  protected loadRelationshipsOptions(): void {
    this.citaService
      .query()
      .pipe(map((res: HttpResponse<ICita[]>) => res.body ?? []))
      .pipe(map((citas: ICita[]) => this.citaService.addCitaToCollectionIfMissing(citas, this.editForm.get('cita')!.value)))
      .subscribe((citas: ICita[]) => (this.citasSharedCollection = citas));
  }

  protected createFromForm(): IPago {
    return {
      ...new Pago(),
      id: this.editForm.get(['id'])!.value,
      monto: this.editForm.get(['monto'])!.value,
      fechaPago: this.editForm.get(['fechaPago'])!.value ? dayjs(this.editForm.get(['fechaPago'])!.value, DATE_TIME_FORMAT) : undefined,
      metodoPago: this.editForm.get(['metodoPago'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      cita: this.editForm.get(['cita'])!.value,
    };
  }
}
