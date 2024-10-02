import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImagen } from '../imagen.model';
import { ImagenService } from '../service/imagen.service';

@Component({
  templateUrl: './imagen-delete-dialog.component.html',
})
export class ImagenDeleteDialogComponent {
  imagen?: IImagen;

  constructor(protected imagenService: ImagenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imagenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
