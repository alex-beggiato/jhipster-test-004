import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IE, E } from '../e.model';
import { EService } from '../service/e.service';

@Injectable({ providedIn: 'root' })
export class ERoutingResolveService implements Resolve<IE> {
  constructor(protected service: EService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IE> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((e: HttpResponse<E>) => {
          if (e.body) {
            return of(e.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new E());
  }
}
