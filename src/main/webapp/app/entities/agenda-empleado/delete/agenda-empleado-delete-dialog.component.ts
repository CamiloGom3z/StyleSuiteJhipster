import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgendaEmpleado } from '../agenda-empleado.model';
import { AgendaEmpleadoService } from '../service/agenda-empleado.service';

@Component({
  templateUrl: './agenda-empleado-delete-dialog.component.html',
})
export class AgendaEmpleadoDeleteDialogComponent {
  agendaEmpleado?: IAgendaEmpleado;

  constructor(protected agendaEmpleadoService: AgendaEmpleadoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agendaEmpleadoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
