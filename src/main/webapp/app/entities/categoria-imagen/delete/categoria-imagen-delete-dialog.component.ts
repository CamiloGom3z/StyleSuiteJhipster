import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaImagen } from '../categoria-imagen.model';
import { CategoriaImagenService } from '../service/categoria-imagen.service';

@Component({
  templateUrl: './categoria-imagen-delete-dialog.component.html',
})
export class CategoriaImagenDeleteDialogComponent {
  categoriaImagen?: ICategoriaImagen;

  constructor(protected categoriaImagenService: CategoriaImagenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaImagenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
