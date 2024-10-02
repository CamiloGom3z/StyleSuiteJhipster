import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgendaEmpleado, getAgendaEmpleadoIdentifier } from '../agenda-empleado.model';

export type EntityResponseType = HttpResponse<IAgendaEmpleado>;
export type EntityArrayResponseType = HttpResponse<IAgendaEmpleado[]>;

@Injectable({ providedIn: 'root' })
export class AgendaEmpleadoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agenda-empleados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agendaEmpleado: IAgendaEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agendaEmpleado);
    return this.http
      .post<IAgendaEmpleado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agendaEmpleado: IAgendaEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agendaEmpleado);
    return this.http
      .put<IAgendaEmpleado>(`${this.resourceUrl}/${getAgendaEmpleadoIdentifier(agendaEmpleado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(agendaEmpleado: IAgendaEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agendaEmpleado);
    return this.http
      .patch<IAgendaEmpleado>(`${this.resourceUrl}/${getAgendaEmpleadoIdentifier(agendaEmpleado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgendaEmpleado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgendaEmpleado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAgendaEmpleadoToCollectionIfMissing(
    agendaEmpleadoCollection: IAgendaEmpleado[],
    ...agendaEmpleadosToCheck: (IAgendaEmpleado | null | undefined)[]
  ): IAgendaEmpleado[] {
    const agendaEmpleados: IAgendaEmpleado[] = agendaEmpleadosToCheck.filter(isPresent);
    if (agendaEmpleados.length > 0) {
      const agendaEmpleadoCollectionIdentifiers = agendaEmpleadoCollection.map(
        agendaEmpleadoItem => getAgendaEmpleadoIdentifier(agendaEmpleadoItem)!
      );
      const agendaEmpleadosToAdd = agendaEmpleados.filter(agendaEmpleadoItem => {
        const agendaEmpleadoIdentifier = getAgendaEmpleadoIdentifier(agendaEmpleadoItem);
        if (agendaEmpleadoIdentifier == null || agendaEmpleadoCollectionIdentifiers.includes(agendaEmpleadoIdentifier)) {
          return false;
        }
        agendaEmpleadoCollectionIdentifiers.push(agendaEmpleadoIdentifier);
        return true;
      });
      return [...agendaEmpleadosToAdd, ...agendaEmpleadoCollection];
    }
    return agendaEmpleadoCollection;
  }

  protected convertDateFromClient(agendaEmpleado: IAgendaEmpleado): IAgendaEmpleado {
    return Object.assign({}, agendaEmpleado, {
      fechaInicio: agendaEmpleado.fechaInicio?.isValid() ? agendaEmpleado.fechaInicio.toJSON() : undefined,
      fechaFin: agendaEmpleado.fechaFin?.isValid() ? agendaEmpleado.fechaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((agendaEmpleado: IAgendaEmpleado) => {
        agendaEmpleado.fechaInicio = agendaEmpleado.fechaInicio ? dayjs(agendaEmpleado.fechaInicio) : undefined;
        agendaEmpleado.fechaFin = agendaEmpleado.fechaFin ? dayjs(agendaEmpleado.fechaFin) : undefined;
      });
    }
    return res;
  }
}
