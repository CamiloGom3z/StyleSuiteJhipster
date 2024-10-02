import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICita } from '../cita.model';
import { CitaService } from '../service/cita.service';
import { CitaDeleteDialogComponent } from '../delete/cita-delete-dialog.component';

@Component({
  selector: 'jhi-cita',
  templateUrl: './cita.component.html',
})
export class CitaComponent implements OnInit {
  citas?: ICita[];
  isLoading = false;

  constructor(protected citaService: CitaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.citaService.query().subscribe({
      next: (res: HttpResponse<ICita[]>) => {
        this.isLoading = false;
        this.citas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICita): number {
    return item.id!;
  }

  delete(cita: ICita): void {
    const modalRef = this.modalService.open(CitaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cita = cita;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
