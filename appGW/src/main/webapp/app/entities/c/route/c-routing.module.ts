import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CComponent } from '../list/c.component';
import { CDetailComponent } from '../detail/c-detail.component';
import { CUpdateComponent } from '../update/c-update.component';
import { CRoutingResolveService } from './c-routing-resolve.service';

const cRoute: Routes = [
  {
    path: '',
    component: CComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CDetailComponent,
    resolve: {
      c: CRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CUpdateComponent,
    resolve: {
      c: CRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CUpdateComponent,
    resolve: {
      c: CRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cRoute)],
  exports: [RouterModule],
})
export class CRoutingModule {}
