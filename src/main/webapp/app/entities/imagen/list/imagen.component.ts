import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImagen } from '../imagen.model';
import { ImagenService } from '../service/imagen.service';
import { ImagenDeleteDialogComponent } from '../delete/imagen-delete-dialog.component';

@Component({
  selector: 'jhi-imagen',
  templateUrl: './imagen.component.html',
})
export class ImagenComponent implements OnInit {
  imagens?: IImagen[];
  isLoading = false;

  constructor(protected imagenService: ImagenService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.imagenService.query().subscribe({
      next: (res: HttpResponse<IImagen[]>) => {
        this.isLoading = false;
        this.imagens = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IImagen): number {
    return item.id!;
  }

  delete(imagen: IImagen): void {
    const modalRef = this.modalService.open(ImagenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.imagen = imagen;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
