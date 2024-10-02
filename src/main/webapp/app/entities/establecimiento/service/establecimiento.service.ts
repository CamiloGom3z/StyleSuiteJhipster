import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEstablecimiento, getEstablecimientoIdentifier } from '../establecimiento.model';

export type EntityResponseType = HttpResponse<IEstablecimiento>;
export type EntityArrayResponseType = HttpResponse<IEstablecimiento[]>;

@Injectable({ providedIn: 'root' })
export class EstablecimientoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/establecimientos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(establecimiento: IEstablecimiento): Observable<EntityResponseType> {
    return this.http.post<IEstablecimiento>(this.resourceUrl, establecimiento, { observe: 'response' });
  }

  update(establecimiento: IEstablecimiento): Observable<EntityResponseType> {
    return this.http.put<IEstablecimiento>(
      `${this.resourceUrl}/${getEstablecimientoIdentifier(establecimiento) as number}`,
      establecimiento,
      { observe: 'response' }
    );
  }

  partialUpdate(establecimiento: IEstablecimiento): Observable<EntityResponseType> {
    return this.http.patch<IEstablecimiento>(
      `${this.resourceUrl}/${getEstablecimientoIdentifier(establecimiento) as number}`,
      establecimiento,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEstablecimiento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEstablecimiento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEstablecimientoToCollectionIfMissing(
    establecimientoCollection: IEstablecimiento[],
    ...establecimientosToCheck: (IEstablecimiento | null | undefined)[]
  ): IEstablecimiento[] {
    const establecimientos: IEstablecimiento[] = establecimientosToCheck.filter(isPresent);
    if (establecimientos.length > 0) {
      const establecimientoCollectionIdentifiers = establecimientoCollection.map(
        establecimientoItem => getEstablecimientoIdentifier(establecimientoItem)!
      );
      const establecimientosToAdd = establecimientos.filter(establecimientoItem => {
        const establecimientoIdentifier = getEstablecimientoIdentifier(establecimientoItem);
        if (establecimientoIdentifier == null || establecimientoCollectionIdentifiers.includes(establecimientoIdentifier)) {
          return false;
        }
        establecimientoCollectionIdentifiers.push(establecimientoIdentifier);
        return true;
      });
      return [...establecimientosToAdd, ...establecimientoCollection];
    }
    return establecimientoCollection;
  }
}
