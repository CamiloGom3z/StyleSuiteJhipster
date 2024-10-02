import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgendaEmpleado, AgendaEmpleado } from '../agenda-empleado.model';
import { AgendaEmpleadoService } from '../service/agenda-empleado.service';

@Injectable({ providedIn: 'root' })
export class AgendaEmpleadoRoutingResolveService implements Resolve<IAgendaEmpleado> {
  constructor(protected service: AgendaEmpleadoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgendaEmpleado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((agendaEmpleado: HttpResponse<AgendaEmpleado>) => {
          if (agendaEmpleado.body) {
            return of(agendaEmpleado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AgendaEmpleado());
  }
}
