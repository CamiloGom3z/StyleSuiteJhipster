import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImagen, getImagenIdentifier } from '../imagen.model';

export type EntityResponseType = HttpResponse<IImagen>;
export type EntityArrayResponseType = HttpResponse<IImagen[]>;

@Injectable({ providedIn: 'root' })
export class ImagenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/imagens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imagen: IImagen): Observable<EntityResponseType> {
    return this.http.post<IImagen>(this.resourceUrl, imagen, { observe: 'response' });
  }

  update(imagen: IImagen): Observable<EntityResponseType> {
    return this.http.put<IImagen>(`${this.resourceUrl}/${getImagenIdentifier(imagen) as number}`, imagen, { observe: 'response' });
  }

  partialUpdate(imagen: IImagen): Observable<EntityResponseType> {
    return this.http.patch<IImagen>(`${this.resourceUrl}/${getImagenIdentifier(imagen) as number}`, imagen, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImagen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImagen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addImagenToCollectionIfMissing(imagenCollection: IImagen[], ...imagensToCheck: (IImagen | null | undefined)[]): IImagen[] {
    const imagens: IImagen[] = imagensToCheck.filter(isPresent);
    if (imagens.length > 0) {
      const imagenCollectionIdentifiers = imagenCollection.map(imagenItem => getImagenIdentifier(imagenItem)!);
      const imagensToAdd = imagens.filter(imagenItem => {
        const imagenIdentifier = getImagenIdentifier(imagenItem);
        if (imagenIdentifier == null || imagenCollectionIdentifiers.includes(imagenIdentifier)) {
          return false;
        }
        imagenCollectionIdentifiers.push(imagenIdentifier);
        return true;
      });
      return [...imagensToAdd, ...imagenCollection];
    }
    return imagenCollection;
  }
}
