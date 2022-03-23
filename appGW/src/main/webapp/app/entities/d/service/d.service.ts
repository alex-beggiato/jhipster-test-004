import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ID, getDIdentifier } from '../d.model';

export type EntityResponseType = HttpResponse<ID>;
export type EntityArrayResponseType = HttpResponse<ID[]>;

@Injectable({ providedIn: 'root' })
export class DService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(d: ID): Observable<EntityResponseType> {
    return this.http.post<ID>(this.resourceUrl, d, { observe: 'response' });
  }

  update(d: ID): Observable<EntityResponseType> {
    return this.http.put<ID>(`${this.resourceUrl}/${getDIdentifier(d) as number}`, d, { observe: 'response' });
  }

  partialUpdate(d: ID): Observable<EntityResponseType> {
    return this.http.patch<ID>(`${this.resourceUrl}/${getDIdentifier(d) as number}`, d, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ID>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ID[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDToCollectionIfMissing(dCollection: ID[], ...dSToCheck: (ID | null | undefined)[]): ID[] {
    const dS: ID[] = dSToCheck.filter(isPresent);
    if (dS.length > 0) {
      const dCollectionIdentifiers = dCollection.map(dItem => getDIdentifier(dItem)!);
      const dSToAdd = dS.filter(dItem => {
        const dIdentifier = getDIdentifier(dItem);
        if (dIdentifier == null || dCollectionIdentifiers.includes(dIdentifier)) {
          return false;
        }
        dCollectionIdentifiers.push(dIdentifier);
        return true;
      });
      return [...dSToAdd, ...dCollection];
    }
    return dCollection;
  }
}
