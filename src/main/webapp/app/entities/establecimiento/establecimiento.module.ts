import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EstablecimientoComponent } from './list/establecimiento.component';
import { EstablecimientoDetailComponent } from './detail/establecimiento-detail.component';
import { EstablecimientoUpdateComponent } from './update/establecimiento-update.component';
import { EstablecimientoDeleteDialogComponent } from './delete/establecimiento-delete-dialog.component';
import { EstablecimientoRoutingModule } from './route/establecimiento-routing.module';

@NgModule({
  imports: [SharedModule, EstablecimientoRoutingModule],
  declarations: [
    EstablecimientoComponent,
    EstablecimientoDetailComponent,
    EstablecimientoUpdateComponent,
    EstablecimientoDeleteDialogComponent,
  ],
  entryComponents: [EstablecimientoDeleteDialogComponent],
})
export class EstablecimientoModule {}
