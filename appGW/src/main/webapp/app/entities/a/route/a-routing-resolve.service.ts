import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IA, A } from '../a.model';
import { AService } from '../service/a.service';

@Injectable({ providedIn: 'root' })
export class ARoutingResolveService implements Resolve<IA> {
  constructor(protected service: AService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IA> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((a: HttpResponse<A>) => {
          if (a.body) {
            return of(a.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new A());
  }
}
