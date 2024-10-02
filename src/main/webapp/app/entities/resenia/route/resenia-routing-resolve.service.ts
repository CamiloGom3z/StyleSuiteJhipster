import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResenia, Resenia } from '../resenia.model';
import { ReseniaService } from '../service/resenia.service';

@Injectable({ providedIn: 'root' })
export class ReseniaRoutingResolveService implements Resolve<IResenia> {
  constructor(protected service: ReseniaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResenia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resenia: HttpResponse<Resenia>) => {
          if (resenia.body) {
            return of(resenia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Resenia());
  }
}
