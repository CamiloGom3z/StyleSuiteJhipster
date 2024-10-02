import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaImagen, getCategoriaImagenIdentifier } from '../categoria-imagen.model';

export type EntityResponseType = HttpResponse<ICategoriaImagen>;
export type EntityArrayResponseType = HttpResponse<ICategoriaImagen[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaImagenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-imagens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaImagen: ICategoriaImagen): Observable<EntityResponseType> {
    return this.http.post<ICategoriaImagen>(this.resourceUrl, categoriaImagen, { observe: 'response' });
  }

  update(categoriaImagen: ICategoriaImagen): Observable<EntityResponseType> {
    return this.http.put<ICategoriaImagen>(
      `${this.resourceUrl}/${getCategoriaImagenIdentifier(categoriaImagen) as number}`,
      categoriaImagen,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaImagen: ICategoriaImagen): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaImagen>(
      `${this.resourceUrl}/${getCategoriaImagenIdentifier(categoriaImagen) as number}`,
      categoriaImagen,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaImagen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaImagen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategoriaImagenToCollectionIfMissing(
    categoriaImagenCollection: ICategoriaImagen[],
    ...categoriaImagensToCheck: (ICategoriaImagen | null | undefined)[]
  ): ICategoriaImagen[] {
    const categoriaImagens: ICategoriaImagen[] = categoriaImagensToCheck.filter(isPresent);
    if (categoriaImagens.length > 0) {
      const categoriaImagenCollectionIdentifiers = categoriaImagenCollection.map(
        categoriaImagenItem => getCategoriaImagenIdentifier(categoriaImagenItem)!
      );
      const categoriaImagensToAdd = categoriaImagens.filter(categoriaImagenItem => {
        const categoriaImagenIdentifier = getCategoriaImagenIdentifier(categoriaImagenItem);
        if (categoriaImagenIdentifier == null || categoriaImagenCollectionIdentifiers.includes(categoriaImagenIdentifier)) {
          return false;
        }
        categoriaImagenCollectionIdentifiers.push(categoriaImagenIdentifier);
        return true;
      });
      return [...categoriaImagensToAdd, ...categoriaImagenCollection];
    }
    return categoriaImagenCollection;
  }
}
