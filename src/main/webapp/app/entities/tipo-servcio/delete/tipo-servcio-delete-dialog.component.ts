import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoServcio } from '../tipo-servcio.model';
import { TipoServcioService } from '../service/tipo-servcio.service';

@Component({
  templateUrl: './tipo-servcio-delete-dialog.component.html',
})
export class TipoServcioDeleteDialogComponent {
  tipoServcio?: ITipoServcio;

  constructor(protected tipoServcioService: TipoServcioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoServcioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
