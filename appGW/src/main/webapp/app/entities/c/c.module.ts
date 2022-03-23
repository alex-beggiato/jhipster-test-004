import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CComponent } from './list/c.component';
import { CDetailComponent } from './detail/c-detail.component';
import { CUpdateComponent } from './update/c-update.component';
import { CDeleteDialogComponent } from './delete/c-delete-dialog.component';
import { CRoutingModule } from './route/c-routing.module';

@NgModule({
  imports: [SharedModule, CRoutingModule],
  declarations: [CComponent, CDetailComponent, CUpdateComponent, CDeleteDialogComponent],
  entryComponents: [CDeleteDialogComponent],
})
export class CModule {}
