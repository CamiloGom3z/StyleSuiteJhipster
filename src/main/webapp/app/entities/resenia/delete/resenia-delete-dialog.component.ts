import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResenia } from '../resenia.model';
import { ReseniaService } from '../service/resenia.service';

@Component({
  templateUrl: './resenia-delete-dialog.component.html',
})
export class ReseniaDeleteDialogComponent {
  resenia?: IResenia;

  constructor(protected reseniaService: ReseniaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reseniaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
