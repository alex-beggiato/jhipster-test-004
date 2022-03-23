import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ID, D } from '../d.model';
import { DService } from '../service/d.service';

@Injectable({ providedIn: 'root' })
export class DRoutingResolveService implements Resolve<ID> {
  constructor(protected service: DService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ID> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((d: HttpResponse<D>) => {
          if (d.body) {
            return of(d.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new D());
  }
}
