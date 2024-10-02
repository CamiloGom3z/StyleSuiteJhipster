import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICargo, getCargoIdentifier } from '../cargo.model';

export type EntityResponseType = HttpResponse<ICargo>;
export type EntityArrayResponseType = HttpResponse<ICargo[]>;

@Injectable({ providedIn: 'root' })
export class CargoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cargos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cargo: ICargo): Observable<EntityResponseType> {
    return this.http.post<ICargo>(this.resourceUrl, cargo, { observe: 'response' });
  }

  update(cargo: ICargo): Observable<EntityResponseType> {
    return this.http.put<ICargo>(`${this.resourceUrl}/${getCargoIdentifier(cargo) as number}`, cargo, { observe: 'response' });
  }

  partialUpdate(cargo: ICargo): Observable<EntityResponseType> {
    return this.http.patch<ICargo>(`${this.resourceUrl}/${getCargoIdentifier(cargo) as number}`, cargo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICargo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICargo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCargoToCollectionIfMissing(cargoCollection: ICargo[], ...cargosToCheck: (ICargo | null | undefined)[]): ICargo[] {
    const cargos: ICargo[] = cargosToCheck.filter(isPresent);
    if (cargos.length > 0) {
      const cargoCollectionIdentifiers = cargoCollection.map(cargoItem => getCargoIdentifier(cargoItem)!);
      const cargosToAdd = cargos.filter(cargoItem => {
        const cargoIdentifier = getCargoIdentifier(cargoItem);
        if (cargoIdentifier == null || cargoCollectionIdentifiers.includes(cargoIdentifier)) {
          return false;
        }
        cargoCollectionIdentifiers.push(cargoIdentifier);
        return true;
      });
      return [...cargosToAdd, ...cargoCollection];
    }
    return cargoCollection;
  }
}
