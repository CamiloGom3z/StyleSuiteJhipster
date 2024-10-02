import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResenia, getReseniaIdentifier } from '../resenia.model';

export type EntityResponseType = HttpResponse<IResenia>;
export type EntityArrayResponseType = HttpResponse<IResenia[]>;

@Injectable({ providedIn: 'root' })
export class ReseniaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resenias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resenia: IResenia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resenia);
    return this.http
      .post<IResenia>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(resenia: IResenia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resenia);
    return this.http
      .put<IResenia>(`${this.resourceUrl}/${getReseniaIdentifier(resenia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(resenia: IResenia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resenia);
    return this.http
      .patch<IResenia>(`${this.resourceUrl}/${getReseniaIdentifier(resenia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IResenia>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResenia[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReseniaToCollectionIfMissing(reseniaCollection: IResenia[], ...reseniasToCheck: (IResenia | null | undefined)[]): IResenia[] {
    const resenias: IResenia[] = reseniasToCheck.filter(isPresent);
    if (resenias.length > 0) {
      const reseniaCollectionIdentifiers = reseniaCollection.map(reseniaItem => getReseniaIdentifier(reseniaItem)!);
      const reseniasToAdd = resenias.filter(reseniaItem => {
        const reseniaIdentifier = getReseniaIdentifier(reseniaItem);
        if (reseniaIdentifier == null || reseniaCollectionIdentifiers.includes(reseniaIdentifier)) {
          return false;
        }
        reseniaCollectionIdentifiers.push(reseniaIdentifier);
        return true;
      });
      return [...reseniasToAdd, ...reseniaCollection];
    }
    return reseniaCollection;
  }

  protected convertDateFromClient(resenia: IResenia): IResenia {
    return Object.assign({}, resenia, {
      fecha: resenia.fecha?.isValid() ? resenia.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((resenia: IResenia) => {
        resenia.fecha = resenia.fecha ? dayjs(resenia.fecha) : undefined;
      });
    }
    return res;
  }
}
