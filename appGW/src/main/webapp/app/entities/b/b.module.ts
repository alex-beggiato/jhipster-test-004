import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BComponent } from './list/b.component';
import { BDetailComponent } from './detail/b-detail.component';
import { BUpdateComponent } from './update/b-update.component';
import { BDeleteDialogComponent } from './delete/b-delete-dialog.component';
import { BRoutingModule } from './route/b-routing.module';

@NgModule({
  imports: [SharedModule, BRoutingModule],
  declarations: [BComponent, BDetailComponent, BUpdateComponent, BDeleteDialogComponent],
  entryComponents: [BDeleteDialogComponent],
})
export class BModule {}
