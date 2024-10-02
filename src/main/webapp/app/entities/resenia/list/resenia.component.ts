import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResenia } from '../resenia.model';
import { ReseniaService } from '../service/resenia.service';
import { ReseniaDeleteDialogComponent } from '../delete/resenia-delete-dialog.component';

@Component({
  selector: 'jhi-resenia',
  templateUrl: './resenia.component.html',
})
export class ReseniaComponent implements OnInit {
  resenias?: IResenia[];
  isLoading = false;

  constructor(protected reseniaService: ReseniaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.reseniaService.query().subscribe({
      next: (res: HttpResponse<IResenia[]>) => {
        this.isLoading = false;
        this.resenias = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IResenia): number {
    return item.id!;
  }

  delete(resenia: IResenia): void {
    const modalRef = this.modalService.open(ReseniaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resenia = resenia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
