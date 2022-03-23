import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DComponent } from '../list/d.component';
import { DDetailComponent } from '../detail/d-detail.component';
import { DUpdateComponent } from '../update/d-update.component';
import { DRoutingResolveService } from './d-routing-resolve.service';

const dRoute: Routes = [
  {
    path: '',
    component: DComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DDetailComponent,
    resolve: {
      d: DRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DUpdateComponent,
    resolve: {
      d: DRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DUpdateComponent,
    resolve: {
      d: DRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dRoute)],
  exports: [RouterModule],
})
export class DRoutingModule {}
