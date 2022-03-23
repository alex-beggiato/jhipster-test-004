import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EComponent } from '../list/e.component';
import { EDetailComponent } from '../detail/e-detail.component';
import { EUpdateComponent } from '../update/e-update.component';
import { ERoutingResolveService } from './e-routing-resolve.service';

const eRoute: Routes = [
  {
    path: '',
    component: EComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EDetailComponent,
    resolve: {
      e: ERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EUpdateComponent,
    resolve: {
      e: ERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EUpdateComponent,
    resolve: {
      e: ERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eRoute)],
  exports: [RouterModule],
})
export class ERoutingModule {}
