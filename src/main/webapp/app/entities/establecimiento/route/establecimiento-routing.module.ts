import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EstablecimientoComponent } from '../list/establecimiento.component';
import { EstablecimientoDetailComponent } from '../detail/establecimiento-detail.component';
import { EstablecimientoUpdateComponent } from '../update/establecimiento-update.component';
import { EstablecimientoRoutingResolveService } from './establecimiento-routing-resolve.service';

const establecimientoRoute: Routes = [
  {
    path: '',
    component: EstablecimientoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EstablecimientoDetailComponent,
    resolve: {
      establecimiento: EstablecimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EstablecimientoUpdateComponent,
    resolve: {
      establecimiento: EstablecimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EstablecimientoUpdateComponent,
    resolve: {
      establecimiento: EstablecimientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(establecimientoRoute)],
  exports: [RouterModule],
})
export class EstablecimientoRoutingModule {}
