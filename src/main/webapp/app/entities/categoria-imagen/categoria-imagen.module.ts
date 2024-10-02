import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoriaImagenComponent } from './list/categoria-imagen.component';
import { CategoriaImagenDetailComponent } from './detail/categoria-imagen-detail.component';
import { CategoriaImagenUpdateComponent } from './update/categoria-imagen-update.component';
import { CategoriaImagenDeleteDialogComponent } from './delete/categoria-imagen-delete-dialog.component';
import { CategoriaImagenRoutingModule } from './route/categoria-imagen-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaImagenRoutingModule],
  declarations: [
    CategoriaImagenComponent,
    CategoriaImagenDetailComponent,
    CategoriaImagenUpdateComponent,
    CategoriaImagenDeleteDialogComponent,
  ],
  entryComponents: [CategoriaImagenDeleteDialogComponent],
})
export class CategoriaImagenModule {}
