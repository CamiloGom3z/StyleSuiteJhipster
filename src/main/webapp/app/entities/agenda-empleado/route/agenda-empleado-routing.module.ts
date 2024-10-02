import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgendaEmpleadoComponent } from '../list/agenda-empleado.component';
import { AgendaEmpleadoDetailComponent } from '../detail/agenda-empleado-detail.component';
import { AgendaEmpleadoUpdateComponent } from '../update/agenda-empleado-update.component';
import { AgendaEmpleadoRoutingResolveService } from './agenda-empleado-routing-resolve.service';

const agendaEmpleadoRoute: Routes = [
  {
    path: '',
    component: AgendaEmpleadoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgendaEmpleadoDetailComponent,
    resolve: {
      agendaEmpleado: AgendaEmpleadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgendaEmpleadoUpdateComponent,
    resolve: {
      agendaEmpleado: AgendaEmpleadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgendaEmpleadoUpdateComponent,
    resolve: {
      agendaEmpleado: AgendaEmpleadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(agendaEmpleadoRoute)],
  exports: [RouterModule],
})
export class AgendaEmpleadoRoutingModule {}
