import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IC, getCIdentifier } from '../c.model';

export type EntityResponseType = HttpResponse<IC>;
export type EntityArrayResponseType = HttpResponse<IC[]>;

@Injectable({ providedIn: 'root' })
export class CService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(c: IC): Observable<EntityResponseType> {
    return this.http.post<IC>(this.resourceUrl, c, { observe: 'response' });
  }

  update(c: IC): Observable<EntityResponseType> {
    return this.http.put<IC>(`${this.resourceUrl}/${getCIdentifier(c) as number}`, c, { observe: 'response' });
  }

  partialUpdate(c: IC): Observable<EntityResponseType> {
    return this.http.patch<IC>(`${this.resourceUrl}/${getCIdentifier(c) as number}`, c, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IC>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IC[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCToCollectionIfMissing(cCollection: IC[], ...cSToCheck: (IC | null | undefined)[]): IC[] {
    const cS: IC[] = cSToCheck.filter(isPresent);
    if (cS.length > 0) {
      const cCollectionIdentifiers = cCollection.map(cItem => getCIdentifier(cItem)!);
      const cSToAdd = cS.filter(cItem => {
        const cIdentifier = getCIdentifier(cItem);
        if (cIdentifier == null || cCollectionIdentifiers.includes(cIdentifier)) {
          return false;
        }
        cCollectionIdentifiers.push(cIdentifier);
        return true;
      });
      return [...cSToAdd, ...cCollection];
    }
    return cCollection;
  }
}
