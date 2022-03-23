import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IB, getBIdentifier } from '../b.model';

export type EntityResponseType = HttpResponse<IB>;
export type EntityArrayResponseType = HttpResponse<IB[]>;

@Injectable({ providedIn: 'root' })
export class BService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(b: IB): Observable<EntityResponseType> {
    return this.http.post<IB>(this.resourceUrl, b, { observe: 'response' });
  }

  update(b: IB): Observable<EntityResponseType> {
    return this.http.put<IB>(`${this.resourceUrl}/${getBIdentifier(b) as number}`, b, { observe: 'response' });
  }

  partialUpdate(b: IB): Observable<EntityResponseType> {
    return this.http.patch<IB>(`${this.resourceUrl}/${getBIdentifier(b) as number}`, b, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IB>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IB[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBToCollectionIfMissing(bCollection: IB[], ...bSToCheck: (IB | null | undefined)[]): IB[] {
    const bS: IB[] = bSToCheck.filter(isPresent);
    if (bS.length > 0) {
      const bCollectionIdentifiers = bCollection.map(bItem => getBIdentifier(bItem)!);
      const bSToAdd = bS.filter(bItem => {
        const bIdentifier = getBIdentifier(bItem);
        if (bIdentifier == null || bCollectionIdentifiers.includes(bIdentifier)) {
          return false;
        }
        bCollectionIdentifiers.push(bIdentifier);
        return true;
      });
      return [...bSToAdd, ...bCollection];
    }
    return bCollection;
  }
}
