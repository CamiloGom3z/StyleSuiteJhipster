import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductos, getProductosIdentifier } from '../productos.model';

export type EntityResponseType = HttpResponse<IProductos>;
export type EntityArrayResponseType = HttpResponse<IProductos[]>;

@Injectable({ providedIn: 'root' })
export class ProductosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productos: IProductos): Observable<EntityResponseType> {
    return this.http.post<IProductos>(this.resourceUrl, productos, { observe: 'response' });
  }

  update(productos: IProductos): Observable<EntityResponseType> {
    return this.http.put<IProductos>(`${this.resourceUrl}/${getProductosIdentifier(productos) as number}`, productos, {
      observe: 'response',
    });
  }

  partialUpdate(productos: IProductos): Observable<EntityResponseType> {
    return this.http.patch<IProductos>(`${this.resourceUrl}/${getProductosIdentifier(productos) as number}`, productos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductosToCollectionIfMissing(
    productosCollection: IProductos[],
    ...productosToCheck: (IProductos | null | undefined)[]
  ): IProductos[] {
    const productos: IProductos[] = productosToCheck.filter(isPresent);
    if (productos.length > 0) {
      const productosCollectionIdentifiers = productosCollection.map(productosItem => getProductosIdentifier(productosItem)!);
      const productosToAdd = productos.filter(productosItem => {
        const productosIdentifier = getProductosIdentifier(productosItem);
        if (productosIdentifier == null || productosCollectionIdentifiers.includes(productosIdentifier)) {
          return false;
        }
        productosCollectionIdentifiers.push(productosIdentifier);
        return true;
      });
      return [...productosToAdd, ...productosCollection];
    }
    return productosCollection;
  }
}
