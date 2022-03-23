import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IE, getEIdentifier } from '../e.model';

export type EntityResponseType = HttpResponse<IE>;
export type EntityArrayResponseType = HttpResponse<IE[]>;

@Injectable({ providedIn: 'root' })
export class EService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/es');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(e: IE): Observable<EntityResponseType> {
    return this.http.post<IE>(this.resourceUrl, e, { observe: 'response' });
  }

  update(e: IE): Observable<EntityResponseType> {
    return this.http.put<IE>(`${this.resourceUrl}/${getEIdentifier(e) as number}`, e, { observe: 'response' });
  }

  partialUpdate(e: IE): Observable<EntityResponseType> {
    return this.http.patch<IE>(`${this.resourceUrl}/${getEIdentifier(e) as number}`, e, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IE>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IE[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEToCollectionIfMissing(eCollection: IE[], ...eSToCheck: (IE | null | undefined)[]): IE[] {
    const eS: IE[] = eSToCheck.filter(isPresent);
    if (eS.length > 0) {
      const eCollectionIdentifiers = eCollection.map(eItem => getEIdentifier(eItem)!);
      const eSToAdd = eS.filter(eItem => {
        const eIdentifier = getEIdentifier(eItem);
        if (eIdentifier == null || eCollectionIdentifiers.includes(eIdentifier)) {
          return false;
        }
        eCollectionIdentifiers.push(eIdentifier);
        return true;
      });
      return [...eSToAdd, ...eCollection];
    }
    return eCollection;
  }
}
