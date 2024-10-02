import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IResenia, Resenia } from '../resenia.model';
import { ReseniaService } from '../service/resenia.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-resenia-update',
  templateUrl: './resenia-update.component.html',
})
export class ReseniaUpdateComponent implements OnInit {
  isSaving = false;

  serviciosSharedCollection: IServicio[] = [];

  editForm = this.fb.group({
    id: [],
    calificacion: [],
    comentario: [],
    fecha: [],
    servicio: [],
  });

  constructor(
    protected reseniaService: ReseniaService,
    protected servicioService: ServicioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resenia }) => {
      if (resenia.id === undefined) {
        const today = dayjs().startOf('day');
        resenia.fecha = today;
      }

      this.updateForm(resenia);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resenia = this.createFromForm();
    if (resenia.id !== undefined) {
      this.subscribeToSaveResponse(this.reseniaService.update(resenia));
    } else {
      this.subscribeToSaveResponse(this.reseniaService.create(resenia));
    }
  }

  trackServicioById(_index: number, item: IServicio): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResenia>>): void {
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

  protected updateForm(resenia: IResenia): void {
    this.editForm.patchValue({
      id: resenia.id,
      calificacion: resenia.calificacion,
      comentario: resenia.comentario,
      fecha: resenia.fecha ? resenia.fecha.format(DATE_TIME_FORMAT) : null,
      servicio: resenia.servicio,
    });

    this.serviciosSharedCollection = this.servicioService.addServicioToCollectionIfMissing(
      this.serviciosSharedCollection,
      resenia.servicio
    );
  }

  protected loadRelationshipsOptions(): void {
    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing(servicios, this.editForm.get('servicio')!.value)
        )
      )
      .subscribe((servicios: IServicio[]) => (this.serviciosSharedCollection = servicios));
  }

  protected createFromForm(): IResenia {
    return {
      ...new Resenia(),
      id: this.editForm.get(['id'])!.value,
      calificacion: this.editForm.get(['calificacion'])!.value,
      comentario: this.editForm.get(['comentario'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      servicio: this.editForm.get(['servicio'])!.value,
    };
  }
}
