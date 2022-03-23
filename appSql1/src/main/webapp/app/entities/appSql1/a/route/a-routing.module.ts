import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AComponent } from '../list/a.component';
import { ADetailComponent } from '../detail/a-detail.component';
import { AUpdateComponent } from '../update/a-update.component';
import { ARoutingResolveService } from './a-routing-resolve.service';

const aRoute: Routes = [
  {
    path: '',
    component: AComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ADetailComponent,
    resolve: {
      a: ARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AUpdateComponent,
    resolve: {
      a: ARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AUpdateComponent,
    resolve: {
      a: ARoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aRoute)],
  exports: [RouterModule],
})
export class ARoutingModule {}
