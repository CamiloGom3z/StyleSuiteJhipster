import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CitaComponent } from './list/cita.component';
import { CitaDetailComponent } from './detail/cita-detail.component';
import { CitaUpdateComponent } from './update/cita-update.component';
import { CitaDeleteDialogComponent } from './delete/cita-delete-dialog.component';
import { CitaRoutingModule } from './route/cita-routing.module';

@NgModule({
  imports: [SharedModule, CitaRoutingModule],
  declarations: [CitaComponent, CitaDetailComponent, CitaUpdateComponent, CitaDeleteDialogComponent],
  entryComponents: [CitaDeleteDialogComponent],
})
export class CitaModule {}
