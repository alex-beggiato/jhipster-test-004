import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BComponent } from '../list/b.component';
import { BDetailComponent } from '../detail/b-detail.component';
import { BUpdateComponent } from '../update/b-update.component';
import { BRoutingResolveService } from './b-routing-resolve.service';

const bRoute: Routes = [
  {
    path: '',
    component: BComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BDetailComponent,
    resolve: {
      b: BRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BUpdateComponent,
    resolve: {
      b: BRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BUpdateComponent,
    resolve: {
      b: BRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bRoute)],
  exports: [RouterModule],
})
export class BRoutingModule {}
