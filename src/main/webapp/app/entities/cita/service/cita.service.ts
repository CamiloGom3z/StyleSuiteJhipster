import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICita, getCitaIdentifier } from '../cita.model';

export type EntityResponseType = HttpResponse<ICita>;
export type EntityArrayResponseType = HttpResponse<ICita[]>;

@Injectable({ providedIn: 'root' })
export class CitaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/citas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cita: ICita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http
      .post<ICita>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cita: ICita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http
      .put<ICita>(`${this.resourceUrl}/${getCitaIdentifier(cita) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cita: ICita): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cita);
    return this.http
      .patch<ICita>(`${this.resourceUrl}/${getCitaIdentifier(cita) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICita>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICita[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCitaToCollectionIfMissing(citaCollection: ICita[], ...citasToCheck: (ICita | null | undefined)[]): ICita[] {
    const citas: ICita[] = citasToCheck.filter(isPresent);
    if (citas.length > 0) {
      const citaCollectionIdentifiers = citaCollection.map(citaItem => getCitaIdentifier(citaItem)!);
      const citasToAdd = citas.filter(citaItem => {
        const citaIdentifier = getCitaIdentifier(citaItem);
        if (citaIdentifier == null || citaCollectionIdentifiers.includes(citaIdentifier)) {
          return false;
        }
        citaCollectionIdentifiers.push(citaIdentifier);
        return true;
      });
      return [...citasToAdd, ...citaCollection];
    }
    return citaCollection;
  }

  protected convertDateFromClient(cita: ICita): ICita {
    return Object.assign({}, cita, {
      fechaCita: cita.fechaCita?.isValid() ? cita.fechaCita.toJSON() : undefined,
      fechaFinCita: cita.fechaFinCita?.isValid() ? cita.fechaFinCita.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCita = res.body.fechaCita ? dayjs(res.body.fechaCita) : undefined;
      res.body.fechaFinCita = res.body.fechaFinCita ? dayjs(res.body.fechaFinCita) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cita: ICita) => {
        cita.fechaCita = cita.fechaCita ? dayjs(cita.fechaCita) : undefined;
        cita.fechaFinCita = cita.fechaFinCita ? dayjs(cita.fechaFinCita) : undefined;
      });
    }
    return res;
  }
}
