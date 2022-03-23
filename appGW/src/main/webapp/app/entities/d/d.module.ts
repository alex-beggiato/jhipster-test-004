import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DComponent } from './list/d.component';
import { DDetailComponent } from './detail/d-detail.component';
import { DUpdateComponent } from './update/d-update.component';
import { DDeleteDialogComponent } from './delete/d-delete-dialog.component';
import { DRoutingModule } from './route/d-routing.module';

@NgModule({
  imports: [SharedModule, DRoutingModule],
  declarations: [DComponent, DDetailComponent, DUpdateComponent, DDeleteDialogComponent],
  entryComponents: [DDeleteDialogComponent],
})
export class DModule {}
