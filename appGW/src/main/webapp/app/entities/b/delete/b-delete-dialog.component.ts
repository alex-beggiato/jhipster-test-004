import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IB } from '../b.model';
import { BService } from '../service/b.service';

@Component({
  templateUrl: './b-delete-dialog.component.html',
})
export class BDeleteDialogComponent {
  b?: IB;

  constructor(protected bService: BService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
