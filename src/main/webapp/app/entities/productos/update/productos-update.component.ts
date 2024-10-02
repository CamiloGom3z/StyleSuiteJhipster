import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProductos, Productos } from '../productos.model';
import { ProductosService } from '../service/productos.service';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';
import { CategoriaProductoService } from 'app/entities/categoria-producto/service/categoria-producto.service';

@Component({
  selector: 'jhi-productos-update',
  templateUrl: './productos-update.component.html',
})
export class ProductosUpdateComponent implements OnInit {
  isSaving = false;

  categoriaProductosSharedCollection: ICategoriaProducto[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    precio: [],
    cantidad: [],
    categoriaProducto: [],
  });

  constructor(
    protected productosService: ProductosService,
    protected categoriaProductoService: CategoriaProductoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productos }) => {
      this.updateForm(productos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productos = this.createFromForm();
    if (productos.id !== undefined) {
      this.subscribeToSaveResponse(this.productosService.update(productos));
    } else {
      this.subscribeToSaveResponse(this.productosService.create(productos));
    }
  }

  trackCategoriaProductoById(_index: number, item: ICategoriaProducto): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductos>>): void {
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

  protected updateForm(productos: IProductos): void {
    this.editForm.patchValue({
      id: productos.id,
      nombre: productos.nombre,
      descripcion: productos.descripcion,
      precio: productos.precio,
      cantidad: productos.cantidad,
      categoriaProducto: productos.categoriaProducto,
    });

    this.categoriaProductosSharedCollection = this.categoriaProductoService.addCategoriaProductoToCollectionIfMissing(
      this.categoriaProductosSharedCollection,
      productos.categoriaProducto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaProductoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaProducto[]>) => res.body ?? []))
      .pipe(
        map((categoriaProductos: ICategoriaProducto[]) =>
          this.categoriaProductoService.addCategoriaProductoToCollectionIfMissing(
            categoriaProductos,
            this.editForm.get('categoriaProducto')!.value
          )
        )
      )
      .subscribe((categoriaProductos: ICategoriaProducto[]) => (this.categoriaProductosSharedCollection = categoriaProductos));
  }

  protected createFromForm(): IProductos {
    return {
      ...new Productos(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      categoriaProducto: this.editForm.get(['categoriaProducto'])!.value,
    };
  }
}
