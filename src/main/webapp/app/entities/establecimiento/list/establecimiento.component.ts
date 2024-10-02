import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEstablecimiento } from '../establecimiento.model';
import { EstablecimientoService } from '../service/establecimiento.service';
import { EstablecimientoDeleteDialogComponent } from '../delete/establecimiento-delete-dialog.component';

@Component({
  selector: 'jhi-establecimiento',
  templateUrl: './establecimiento.component.html',
})
export class EstablecimientoComponent implements OnInit {
  establecimientos?: IEstablecimiento[];
  isLoading = false;

  constructor(protected establecimientoService: EstablecimientoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.establecimientoService.query().subscribe({
      next: (res: HttpResponse<IEstablecimiento[]>) => {
        this.isLoading = false;
        this.establecimientos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEstablecimiento): number {
    return item.id!;
  }

  delete(establecimiento: IEstablecimiento): void {
    const modalRef = this.modalService.open(EstablecimientoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.establecimiento = establecimiento;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
