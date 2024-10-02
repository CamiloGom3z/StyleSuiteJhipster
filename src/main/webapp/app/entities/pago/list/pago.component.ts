import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPago } from '../pago.model';
import { PagoService } from '../service/pago.service';
import { PagoDeleteDialogComponent } from '../delete/pago-delete-dialog.component';

@Component({
  selector: 'jhi-pago',
  templateUrl: './pago.component.html',
})
export class PagoComponent implements OnInit {
  pagos?: IPago[];
  isLoading = false;

  constructor(protected pagoService: PagoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pagoService.query().subscribe({
      next: (res: HttpResponse<IPago[]>) => {
        this.isLoading = false;
        this.pagos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPago): number {
    return item.id!;
  }

  delete(pago: IPago): void {
    const modalRef = this.modalService.open(PagoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pago = pago;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
