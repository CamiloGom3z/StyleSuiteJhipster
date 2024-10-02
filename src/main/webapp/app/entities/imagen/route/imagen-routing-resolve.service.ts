import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImagen, Imagen } from '../imagen.model';
import { ImagenService } from '../service/imagen.service';

@Injectable({ providedIn: 'root' })
export class ImagenRoutingResolveService implements Resolve<IImagen> {
  constructor(protected service: ImagenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImagen> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imagen: HttpResponse<Imagen>) => {
          if (imagen.body) {
            return of(imagen.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Imagen());
  }
}
