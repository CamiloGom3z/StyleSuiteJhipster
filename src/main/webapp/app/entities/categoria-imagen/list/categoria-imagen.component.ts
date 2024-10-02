import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaImagen } from '../categoria-imagen.model';
import { CategoriaImagenService } from '../service/categoria-imagen.service';
import { CategoriaImagenDeleteDialogComponent } from '../delete/categoria-imagen-delete-dialog.component';

@Component({
  selector: 'jhi-categoria-imagen',
  templateUrl: './categoria-imagen.component.html',
})
export class CategoriaImagenComponent implements OnInit {
  categoriaImagens?: ICategoriaImagen[];
  isLoading = false;

  constructor(protected categoriaImagenService: CategoriaImagenService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.categoriaImagenService.query().subscribe({
      next: (res: HttpResponse<ICategoriaImagen[]>) => {
        this.isLoading = false;
        this.categoriaImagens = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICategoriaImagen): number {
    return item.id!;
  }

  delete(categoriaImagen: ICategoriaImagen): void {
    const modalRef = this.modalService.open(CategoriaImagenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categoriaImagen = categoriaImagen;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
