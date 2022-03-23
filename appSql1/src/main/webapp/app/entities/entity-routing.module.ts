import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'a',
        data: { pageTitle: 'appSql1App.appSql1A.home.title' },
        loadChildren: () => import('./appSql1/a/a.module').then(m => m.AppSql1AModule),
      },
      {
        path: 'b',
        data: { pageTitle: 'appSql1App.appSql1B.home.title' },
        loadChildren: () => import('./appSql1/b/b.module').then(m => m.AppSql1BModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
