import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoServcio } from '../tipo-servcio.model';
import { TipoServcioService } from '../service/tipo-servcio.service';
import { TipoServcioDeleteDialogComponent } from '../delete/tipo-servcio-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-servcio',
  templateUrl: './tipo-servcio.component.html',
})
export class TipoServcioComponent implements OnInit {
  tipoServcios?: ITipoServcio[];
  isLoading = false;

  constructor(protected tipoServcioService: TipoServcioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoServcioService.query().subscribe({
      next: (res: HttpResponse<ITipoServcio[]>) => {
        this.isLoading = false;
        this.tipoServcios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoServcio): number {
    return item.id!;
  }

  delete(tipoServcio: ITipoServcio): void {
    const modalRef = this.modalService.open(TipoServcioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoServcio = tipoServcio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
