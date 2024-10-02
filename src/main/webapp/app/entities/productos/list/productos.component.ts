import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductos } from '../productos.model';
import { ProductosService } from '../service/productos.service';
import { ProductosDeleteDialogComponent } from '../delete/productos-delete-dialog.component';

@Component({
  selector: 'jhi-productos',
  templateUrl: './productos.component.html',
})
export class ProductosComponent implements OnInit {
  productos?: IProductos[];
  isLoading = false;

  constructor(protected productosService: ProductosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productosService.query().subscribe({
      next: (res: HttpResponse<IProductos[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProductos): number {
    return item.id!;
  }

  delete(productos: IProductos): void {
    const modalRef = this.modalService.open(ProductosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productos = productos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
