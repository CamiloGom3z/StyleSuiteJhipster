import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AgendaEmpleadoComponent } from './list/agenda-empleado.component';
import { AgendaEmpleadoDetailComponent } from './detail/agenda-empleado-detail.component';
import { AgendaEmpleadoUpdateComponent } from './update/agenda-empleado-update.component';
import { AgendaEmpleadoDeleteDialogComponent } from './delete/agenda-empleado-delete-dialog.component';
import { AgendaEmpleadoRoutingModule } from './route/agenda-empleado-routing.module';

@NgModule({
  imports: [SharedModule, AgendaEmpleadoRoutingModule],
  declarations: [
    AgendaEmpleadoComponent,
    AgendaEmpleadoDetailComponent,
    AgendaEmpleadoUpdateComponent,
    AgendaEmpleadoDeleteDialogComponent,
  ],
  entryComponents: [AgendaEmpleadoDeleteDialogComponent],
})
export class AgendaEmpleadoModule {}
