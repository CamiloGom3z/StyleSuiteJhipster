import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';
import { PromocionDeleteDialogComponent } from '../delete/promocion-delete-dialog.component';

@Component({
  selector: 'jhi-promocion',
  templateUrl: './promocion.component.html',
})
export class PromocionComponent implements OnInit {
  promocions?: IPromocion[];
  isLoading = false;

  constructor(protected promocionService: PromocionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.promocionService.query().subscribe({
      next: (res: HttpResponse<IPromocion[]>) => {
        this.isLoading = false;
        this.promocions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPromocion): number {
    return item.id!;
  }

  delete(promocion: IPromocion): void {
    const modalRef = this.modalService.open(PromocionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.promocion = promocion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
