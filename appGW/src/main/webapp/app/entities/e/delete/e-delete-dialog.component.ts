import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IE } from '../e.model';
import { EService } from '../service/e.service';

@Component({
  templateUrl: './e-delete-dialog.component.html',
})
export class EDeleteDialogComponent {
  e?: IE;

  constructor(protected eService: EService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
