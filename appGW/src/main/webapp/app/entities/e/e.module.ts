import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EComponent } from './list/e.component';
import { EDetailComponent } from './detail/e-detail.component';
import { EUpdateComponent } from './update/e-update.component';
import { EDeleteDialogComponent } from './delete/e-delete-dialog.component';
import { ERoutingModule } from './route/e-routing.module';

@NgModule({
  imports: [SharedModule, ERoutingModule],
  declarations: [EComponent, EDetailComponent, EUpdateComponent, EDeleteDialogComponent],
  entryComponents: [EDeleteDialogComponent],
})
export class EModule {}
