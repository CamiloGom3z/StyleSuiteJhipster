import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImagenComponent } from './list/imagen.component';
import { ImagenDetailComponent } from './detail/imagen-detail.component';
import { ImagenUpdateComponent } from './update/imagen-update.component';
import { ImagenDeleteDialogComponent } from './delete/imagen-delete-dialog.component';
import { ImagenRoutingModule } from './route/imagen-routing.module';

@NgModule({
  imports: [SharedModule, ImagenRoutingModule],
  declarations: [ImagenComponent, ImagenDetailComponent, ImagenUpdateComponent, ImagenDeleteDialogComponent],
  entryComponents: [ImagenDeleteDialogComponent],
})
export class ImagenModule {}
