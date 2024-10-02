import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductosComponent } from '../list/productos.component';
import { ProductosDetailComponent } from '../detail/productos-detail.component';
import { ProductosUpdateComponent } from '../update/productos-update.component';
import { ProductosRoutingResolveService } from './productos-routing-resolve.service';

const productosRoute: Routes = [
  {
    path: '',
    component: ProductosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductosDetailComponent,
    resolve: {
      productos: ProductosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductosUpdateComponent,
    resolve: {
      productos: ProductosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductosUpdateComponent,
    resolve: {
      productos: ProductosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productosRoute)],
  exports: [RouterModule],
})
export class ProductosRoutingModule {}
