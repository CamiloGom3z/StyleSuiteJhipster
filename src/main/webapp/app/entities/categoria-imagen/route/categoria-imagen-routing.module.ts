import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaImagenComponent } from '../list/categoria-imagen.component';
import { CategoriaImagenDetailComponent } from '../detail/categoria-imagen-detail.component';
import { CategoriaImagenUpdateComponent } from '../update/categoria-imagen-update.component';
import { CategoriaImagenRoutingResolveService } from './categoria-imagen-routing-resolve.service';

const categoriaImagenRoute: Routes = [
  {
    path: '',
    component: CategoriaImagenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaImagenDetailComponent,
    resolve: {
      categoriaImagen: CategoriaImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaImagenUpdateComponent,
    resolve: {
      categoriaImagen: CategoriaImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaImagenUpdateComponent,
    resolve: {
      categoriaImagen: CategoriaImagenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaImagenRoute)],
  exports: [RouterModule],
})
export class CategoriaImagenRoutingModule {}
