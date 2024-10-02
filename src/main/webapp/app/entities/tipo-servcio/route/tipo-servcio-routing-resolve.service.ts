import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoServcio, TipoServcio } from '../tipo-servcio.model';
import { TipoServcioService } from '../service/tipo-servcio.service';

@Injectable({ providedIn: 'root' })
export class TipoServcioRoutingResolveService implements Resolve<ITipoServcio> {
  constructor(protected service: TipoServcioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoServcio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoServcio: HttpResponse<TipoServcio>) => {
          if (tipoServcio.body) {
            return of(tipoServcio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoServcio());
  }
}
