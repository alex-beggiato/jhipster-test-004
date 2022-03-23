import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ID } from '../d.model';
import { DService } from '../service/d.service';

@Component({
  templateUrl: './d-delete-dialog.component.html',
})
export class DDeleteDialogComponent {
  d?: ID;

  constructor(protected dService: DService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
