import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoServcio, TipoServcio } from '../tipo-servcio.model';
import { TipoServcioService } from '../service/tipo-servcio.service';

@Component({
  selector: 'jhi-tipo-servcio-update',
  templateUrl: './tipo-servcio-update.component.html',
})
export class TipoServcioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    valorTipoServicio: [],
  });

  constructor(protected tipoServcioService: TipoServcioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoServcio }) => {
      this.updateForm(tipoServcio);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoServcio = this.createFromForm();
    if (tipoServcio.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoServcioService.update(tipoServcio));
    } else {
      this.subscribeToSaveResponse(this.tipoServcioService.create(tipoServcio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoServcio>>): void {
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

  protected updateForm(tipoServcio: ITipoServcio): void {
    this.editForm.patchValue({
      id: tipoServcio.id,
      nombre: tipoServcio.nombre,
      descripcion: tipoServcio.descripcion,
      valorTipoServicio: tipoServcio.valorTipoServicio,
    });
  }

  protected createFromForm(): ITipoServcio {
    return {
      ...new TipoServcio(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      valorTipoServicio: this.editForm.get(['valorTipoServicio'])!.value,
    };
  }
}
