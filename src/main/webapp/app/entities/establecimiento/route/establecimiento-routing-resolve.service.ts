import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEstablecimiento, Establecimiento } from '../establecimiento.model';
import { EstablecimientoService } from '../service/establecimiento.service';

@Injectable({ providedIn: 'root' })
export class EstablecimientoRoutingResolveService implements Resolve<IEstablecimiento> {
  constructor(protected service: EstablecimientoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEstablecimiento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((establecimiento: HttpResponse<Establecimiento>) => {
          if (establecimiento.body) {
            return of(establecimiento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Establecimiento());
  }
}
