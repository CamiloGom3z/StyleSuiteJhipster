import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEstablecimiento } from '../establecimiento.model';
import { EstablecimientoService } from '../service/establecimiento.service';

@Component({
  templateUrl: './establecimiento-delete-dialog.component.html',
})
export class EstablecimientoDeleteDialogComponent {
  establecimiento?: IEstablecimiento;

  constructor(protected establecimientoService: EstablecimientoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.establecimientoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
