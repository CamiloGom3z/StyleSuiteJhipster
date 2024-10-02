import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

@Component({
  selector: 'jhi-categoria-producto-update',
  templateUrl: './categoria-producto-update.component.html',
})
export class CategoriaProductoUpdateComponent implements OnInit {
  isSaving = false;

  establecimientosSharedCollection: IEstablecimiento[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    establecimiento: [],
  });

  constructor(
    protected categoriaProductoService: CategoriaProductoService,
    protected establecimientoService: EstablecimientoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaProducto }) => {
      this.updateForm(categoriaProducto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaProducto = this.createFromForm();
    if (categoriaProducto.id !== undefined) {
      this.subscribeToSaveResponse(this.categoriaProductoService.update(categoriaProducto));
    } else {
      this.subscribeToSaveResponse(this.categoriaProductoService.create(categoriaProducto));
    }
  }

  trackEstablecimientoById(_index: number, item: IEstablecimiento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaProducto>>): void {
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

  protected updateForm(categoriaProducto: ICategoriaProducto): void {
    this.editForm.patchValue({
      id: categoriaProducto.id,
      nombre: categoriaProducto.nombre,
      descripcion: categoriaProducto.descripcion,
      establecimiento: categoriaProducto.establecimiento,
    });

    this.establecimientosSharedCollection = this.establecimientoService.addEstablecimientoToCollectionIfMissing(
      this.establecimientosSharedCollection,
      categoriaProducto.establecimiento
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

  protected createFromForm(): ICategoriaProducto {
    return {
      ...new CategoriaProducto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      establecimiento: this.editForm.get(['establecimiento'])!.value,
    };
  }
}
