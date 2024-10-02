import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategoriaImagen, CategoriaImagen } from '../categoria-imagen.model';
import { CategoriaImagenService } from '../service/categoria-imagen.service';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

@Component({
  selector: 'jhi-categoria-imagen-update',
  templateUrl: './categoria-imagen-update.component.html',
})
export class CategoriaImagenUpdateComponent implements OnInit {
  isSaving = false;

  establecimientosSharedCollection: IEstablecimiento[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    establecimiento: [],
  });

  constructor(
    protected categoriaImagenService: CategoriaImagenService,
    protected establecimientoService: EstablecimientoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaImagen }) => {
      this.updateForm(categoriaImagen);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaImagen = this.createFromForm();
    if (categoriaImagen.id !== undefined) {
      this.subscribeToSaveResponse(this.categoriaImagenService.update(categoriaImagen));
    } else {
      this.subscribeToSaveResponse(this.categoriaImagenService.create(categoriaImagen));
    }
  }

  trackEstablecimientoById(_index: number, item: IEstablecimiento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaImagen>>): void {
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

  protected updateForm(categoriaImagen: ICategoriaImagen): void {
    this.editForm.patchValue({
      id: categoriaImagen.id,
      nombre: categoriaImagen.nombre,
      descripcion: categoriaImagen.descripcion,
      establecimiento: categoriaImagen.establecimiento,
    });

    this.establecimientosSharedCollection = this.establecimientoService.addEstablecimientoToCollectionIfMissing(
      this.establecimientosSharedCollection,
      categoriaImagen.establecimiento
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

  protected createFromForm(): ICategoriaImagen {
    return {
      ...new CategoriaImagen(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      establecimiento: this.editForm.get(['establecimiento'])!.value,
    };
  }
}
