import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReseniaComponent } from '../list/resenia.component';
import { ReseniaDetailComponent } from '../detail/resenia-detail.component';
import { ReseniaUpdateComponent } from '../update/resenia-update.component';
import { ReseniaRoutingResolveService } from './resenia-routing-resolve.service';

const reseniaRoute: Routes = [
  {
    path: '',
    component: ReseniaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReseniaDetailComponent,
    resolve: {
      resenia: ReseniaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReseniaUpdateComponent,
    resolve: {
      resenia: ReseniaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReseniaUpdateComponent,
    resolve: {
      resenia: ReseniaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reseniaRoute)],
  exports: [RouterModule],
})
export class ReseniaRoutingModule {}
