import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoServcioComponent } from '../list/tipo-servcio.component';
import { TipoServcioDetailComponent } from '../detail/tipo-servcio-detail.component';
import { TipoServcioUpdateComponent } from '../update/tipo-servcio-update.component';
import { TipoServcioRoutingResolveService } from './tipo-servcio-routing-resolve.service';

const tipoServcioRoute: Routes = [
  {
    path: '',
    component: TipoServcioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoServcioDetailComponent,
    resolve: {
      tipoServcio: TipoServcioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoServcioUpdateComponent,
    resolve: {
      tipoServcio: TipoServcioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoServcioUpdateComponent,
    resolve: {
      tipoServcio: TipoServcioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoServcioRoute)],
  exports: [RouterModule],
})
export class TipoServcioRoutingModule {}
