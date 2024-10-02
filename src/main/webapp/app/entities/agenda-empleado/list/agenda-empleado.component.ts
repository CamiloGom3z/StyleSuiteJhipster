import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgendaEmpleado } from '../agenda-empleado.model';
import { AgendaEmpleadoService } from '../service/agenda-empleado.service';
import { AgendaEmpleadoDeleteDialogComponent } from '../delete/agenda-empleado-delete-dialog.component';

@Component({
  selector: 'jhi-agenda-empleado',
  templateUrl: './agenda-empleado.component.html',
})
export class AgendaEmpleadoComponent implements OnInit {
  agendaEmpleados?: IAgendaEmpleado[];
  isLoading = false;

  constructor(protected agendaEmpleadoService: AgendaEmpleadoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.agendaEmpleadoService.query().subscribe({
      next: (res: HttpResponse<IAgendaEmpleado[]>) => {
        this.isLoading = false;
        this.agendaEmpleados = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAgendaEmpleado): number {
    return item.id!;
  }

  delete(agendaEmpleado: IAgendaEmpleado): void {
    const modalRef = this.modalService.open(AgendaEmpleadoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.agendaEmpleado = agendaEmpleado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
