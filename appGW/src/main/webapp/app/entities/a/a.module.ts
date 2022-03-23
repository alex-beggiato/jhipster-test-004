import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AComponent } from './list/a.component';
import { ADetailComponent } from './detail/a-detail.component';
import { AUpdateComponent } from './update/a-update.component';
import { ADeleteDialogComponent } from './delete/a-delete-dialog.component';
import { ARoutingModule } from './route/a-routing.module';

@NgModule({
  imports: [SharedModule, ARoutingModule],
  declarations: [AComponent, ADetailComponent, AUpdateComponent, ADeleteDialogComponent],
  entryComponents: [ADeleteDialogComponent],
})
export class AModule {}
