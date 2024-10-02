import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImagenComponent } from '../list/imagen.component';
import { ImagenDetailComponent } from '../detail/imagen-detail.component';
import { ImagenUpdateComponent } from '../update/imagen-update.component';
import { ImagenRoutingResolveService } from './imagen-routing-resolve.service';

const imagenRoute: Routes = [
  {
    path: '',
    component: ImagenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImagenDetailComponent,
    resolve: {
      imagen: ImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImagenUpdateComponent,
    resolve: {
      imagen: ImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImagenUpdateComponent,
    resolve: {
      imagen: ImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imagenRoute)],
  exports: [RouterModule],
})
export class ImagenRoutingModule {}
