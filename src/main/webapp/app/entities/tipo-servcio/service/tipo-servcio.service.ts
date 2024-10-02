import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoServcio, getTipoServcioIdentifier } from '../tipo-servcio.model';

export type EntityResponseType = HttpResponse<ITipoServcio>;
export type EntityArrayResponseType = HttpResponse<ITipoServcio[]>;

@Injectable({ providedIn: 'root' })
export class TipoServcioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-servcios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoServcio: ITipoServcio): Observable<EntityResponseType> {
    return this.http.post<ITipoServcio>(this.resourceUrl, tipoServcio, { observe: 'response' });
  }

  update(tipoServcio: ITipoServcio): Observable<EntityResponseType> {
    return this.http.put<ITipoServcio>(`${this.resourceUrl}/${getTipoServcioIdentifier(tipoServcio) as number}`, tipoServcio, {
      observe: 'response',
    });
  }

  partialUpdate(tipoServcio: ITipoServcio): Observable<EntityResponseType> {
    return this.http.patch<ITipoServcio>(`${this.resourceUrl}/${getTipoServcioIdentifier(tipoServcio) as number}`, tipoServcio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoServcio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoServcio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoServcioToCollectionIfMissing(
    tipoServcioCollection: ITipoServcio[],
    ...tipoServciosToCheck: (ITipoServcio | null | undefined)[]
  ): ITipoServcio[] {
    const tipoServcios: ITipoServcio[] = tipoServciosToCheck.filter(isPresent);
    if (tipoServcios.length > 0) {
      const tipoServcioCollectionIdentifiers = tipoServcioCollection.map(tipoServcioItem => getTipoServcioIdentifier(tipoServcioItem)!);
      const tipoServciosToAdd = tipoServcios.filter(tipoServcioItem => {
        const tipoServcioIdentifier = getTipoServcioIdentifier(tipoServcioItem);
        if (tipoServcioIdentifier == null || tipoServcioCollectionIdentifiers.includes(tipoServcioIdentifier)) {
          return false;
        }
        tipoServcioCollectionIdentifiers.push(tipoServcioIdentifier);
        return true;
      });
      return [...tipoServciosToAdd, ...tipoServcioCollection];
    }
    return tipoServcioCollection;
  }
}
