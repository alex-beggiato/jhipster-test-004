import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IB, B } from '../b.model';
import { BService } from '../service/b.service';

@Injectable({ providedIn: 'root' })
export class BRoutingResolveService implements Resolve<IB> {
  constructor(protected service: BService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IB> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((b: HttpResponse<B>) => {
          if (b.body) {
            return of(b.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new B());
  }
}
