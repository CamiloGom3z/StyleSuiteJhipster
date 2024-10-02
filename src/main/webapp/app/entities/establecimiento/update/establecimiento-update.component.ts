import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEstablecimiento, Establecimiento } from '../establecimiento.model';
import { EstablecimientoService } from '../service/establecimiento.service';

@Component({
  selector: 'jhi-establecimiento-update',
  templateUrl: './establecimiento-update.component.html',
})
export class EstablecimientoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    nit: [],
    direccion: [],
    telefono: [],
    correoElectronico: [],
  });

  constructor(
    protected establecimientoService: EstablecimientoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ establecimiento }) => {
      this.updateForm(establecimiento);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const establecimiento = this.createFromForm();
    if (establecimiento.id !== undefined) {
      this.subscribeToSaveResponse(this.establecimientoService.update(establecimiento));
    } else {
      this.subscribeToSaveResponse(this.establecimientoService.create(establecimiento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstablecimiento>>): void {
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

  protected updateForm(establecimiento: IEstablecimiento): void {
    this.editForm.patchValue({
      id: establecimiento.id,
      nombre: establecimiento.nombre,
      nit: establecimiento.nit,
      direccion: establecimiento.direccion,
      telefono: establecimiento.telefono,
      correoElectronico: establecimiento.correoElectronico,
    });
  }

  protected createFromForm(): IEstablecimiento {
    return {
      ...new Establecimiento(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      correoElectronico: this.editForm.get(['correoElectronico'])!.value,
    };
  }
}
