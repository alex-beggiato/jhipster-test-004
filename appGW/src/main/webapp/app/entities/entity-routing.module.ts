import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'c',
        data: { pageTitle: 'appGwApp.c.home.title' },
        loadChildren: () => import('./c/c.module').then(m => m.CModule),
      },
      {
        path: 'a',
        data: { pageTitle: 'appGwApp.a.home.title' },
        loadChildren: () => import('./a/a.module').then(m => m.AModule),
      },
      {
        path: 'b',
        data: { pageTitle: 'appGwApp.b.home.title' },
        loadChildren: () => import('./b/b.module').then(m => m.BModule),
      },
      {
        path: 'd',
        data: { pageTitle: 'appGwApp.d.home.title' },
        loadChildren: () => import('./d/d.module').then(m => m.DModule),
      },
      {
        path: 'e',
        data: { pageTitle: 'appGwApp.e.home.title' },
        loadChildren: () => import('./e/e.module').then(m => m.EModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
