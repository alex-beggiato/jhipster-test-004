import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IA, getAIdentifier } from '../a.model';

export type EntityResponseType = HttpResponse<IA>;
export type EntityArrayResponseType = HttpResponse<IA[]>;

@Injectable({ providedIn: 'root' })
export class AService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/as');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(a: IA): Observable<EntityResponseType> {
    return this.http.post<IA>(this.resourceUrl, a, { observe: 'response' });
  }

  update(a: IA): Observable<EntityResponseType> {
    return this.http.put<IA>(`${this.resourceUrl}/${getAIdentifier(a) as number}`, a, { observe: 'response' });
  }

  partialUpdate(a: IA): Observable<EntityResponseType> {
    return this.http.patch<IA>(`${this.resourceUrl}/${getAIdentifier(a) as number}`, a, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IA>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IA[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAToCollectionIfMissing(aCollection: IA[], ...aSToCheck: (IA | null | undefined)[]): IA[] {
    const aS: IA[] = aSToCheck.filter(isPresent);
    if (aS.length > 0) {
      const aCollectionIdentifiers = aCollection.map(aItem => getAIdentifier(aItem)!);
      const aSToAdd = aS.filter(aItem => {
        const aIdentifier = getAIdentifier(aItem);
        if (aIdentifier == null || aCollectionIdentifiers.includes(aIdentifier)) {
          return false;
        }
        aCollectionIdentifiers.push(aIdentifier);
        return true;
      });
      return [...aSToAdd, ...aCollection];
    }
    return aCollection;
  }
}
