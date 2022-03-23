import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'c',
        data: { pageTitle: 'appSql2App.appSql2C.home.title' },
        loadChildren: () => import('./appSql2/c/c.module').then(m => m.AppSql2CModule),
      },
      {
        path: 'e',
        data: { pageTitle: 'appSql2App.appSql2E.home.title' },
        loadChildren: () => import('./appSql2/e/e.module').then(m => m.AppSql2EModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
