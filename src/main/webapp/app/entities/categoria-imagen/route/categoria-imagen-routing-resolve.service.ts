import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaImagen, CategoriaImagen } from '../categoria-imagen.model';
import { CategoriaImagenService } from '../service/categoria-imagen.service';

@Injectable({ providedIn: 'root' })
export class CategoriaImagenRoutingResolveService implements Resolve<ICategoriaImagen> {
  constructor(protected service: CategoriaImagenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaImagen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaImagen: HttpResponse<CategoriaImagen>) => {
          if (categoriaImagen.body) {
            return of(categoriaImagen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategoriaImagen());
  }
}
