import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IImagen, Imagen } from '../imagen.model';
import { ImagenService } from '../service/imagen.service';
import { ICategoriaImagen } from 'app/entities/categoria-imagen/categoria-imagen.model';
import { CategoriaImagenService } from 'app/entities/categoria-imagen/service/categoria-imagen.service';

@Component({
  selector: 'jhi-imagen-update',
  templateUrl: './imagen-update.component.html',
})
export class ImagenUpdateComponent implements OnInit {
  isSaving = false;

  categoriaImagensSharedCollection: ICategoriaImagen[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    urlImagen: [],
    categoriaImagen: [],
  });

  constructor(
    protected imagenService: ImagenService,
    protected categoriaImagenService: CategoriaImagenService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imagen }) => {
      this.updateForm(imagen);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imagen = this.createFromForm();
    if (imagen.id !== undefined) {
      this.subscribeToSaveResponse(this.imagenService.update(imagen));
    } else {
      this.subscribeToSaveResponse(this.imagenService.create(imagen));
    }
  }

  trackCategoriaImagenById(_index: number, item: ICategoriaImagen): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImagen>>): void {
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

  protected updateForm(imagen: IImagen): void {
    this.editForm.patchValue({
      id: imagen.id,
      nombre: imagen.nombre,
      descripcion: imagen.descripcion,
      urlImagen: imagen.urlImagen,
      categoriaImagen: imagen.categoriaImagen,
    });

    this.categoriaImagensSharedCollection = this.categoriaImagenService.addCategoriaImagenToCollectionIfMissing(
      this.categoriaImagensSharedCollection,
      imagen.categoriaImagen
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaImagenService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaImagen[]>) => res.body ?? []))
      .pipe(
        map((categoriaImagens: ICategoriaImagen[]) =>
          this.categoriaImagenService.addCategoriaImagenToCollectionIfMissing(categoriaImagens, this.editForm.get('categoriaImagen')!.value)
        )
      )
      .subscribe((categoriaImagens: ICategoriaImagen[]) => (this.categoriaImagensSharedCollection = categoriaImagens));
  }

  protected createFromForm(): IImagen {
    return {
      ...new Imagen(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      urlImagen: this.editForm.get(['urlImagen'])!.value,
      categoriaImagen: this.editForm.get(['categoriaImagen'])!.value,
    };
  }
}
