import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IA } from '../a.model';
import { AService } from '../service/a.service';

@Component({
  templateUrl: './a-delete-dialog.component.html',
})
export class ADeleteDialogComponent {
  a?: IA;

  constructor(protected aService: AService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
