import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgenda, getAgendaIdentifier } from '../agenda.model';

export type EntityResponseType = HttpResponse<IAgenda>;
export type EntityArrayResponseType = HttpResponse<IAgenda[]>;

@Injectable({ providedIn: 'root' })
export class AgendaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agenda');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agenda: IAgenda): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agenda);
    return this.http
      .post<IAgenda>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agenda: IAgenda): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agenda);
    return this.http
      .put<IAgenda>(`${this.resourceUrl}/${getAgendaIdentifier(agenda) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(agenda: IAgenda): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agenda);
    return this.http
      .patch<IAgenda>(`${this.resourceUrl}/${getAgendaIdentifier(agenda) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgenda>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgenda[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAgendaToCollectionIfMissing(agendaCollection: IAgenda[], ...agendaToCheck: (IAgenda | null | undefined)[]): IAgenda[] {
    const agenda: IAgenda[] = agendaToCheck.filter(isPresent);
    if (agenda.length > 0) {
      const agendaCollectionIdentifiers = agendaCollection.map(agendaItem => getAgendaIdentifier(agendaItem)!);
      const agendaToAdd = agenda.filter(agendaItem => {
        const agendaIdentifier = getAgendaIdentifier(agendaItem);
        if (agendaIdentifier == null || agendaCollectionIdentifiers.includes(agendaIdentifier)) {
          return false;
        }
        agendaCollectionIdentifiers.push(agendaIdentifier);
        return true;
      });
      return [...agendaToAdd, ...agendaCollection];
    }
    return agendaCollection;
  }

  protected convertDateFromClient(agenda: IAgenda): IAgenda {
    return Object.assign({}, agenda, {
      fechaInicio: agenda.fechaInicio?.isValid() ? agenda.fechaInicio.toJSON() : undefined,
      fechaFin: agenda.fechaFin?.isValid() ? agenda.fechaFin.toJSON() : undefined,
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
      res.body.forEach((agenda: IAgenda) => {
        agenda.fechaInicio = agenda.fechaInicio ? dayjs(agenda.fechaInicio) : undefined;
        agenda.fechaFin = agenda.fechaFin ? dayjs(agenda.fechaFin) : undefined;
      });
    }
    return res;
  }
}
