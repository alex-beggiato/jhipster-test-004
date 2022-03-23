import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IC } from '../c.model';
import { CService } from '../service/c.service';

@Component({
  templateUrl: './c-delete-dialog.component.html',
})
export class CDeleteDialogComponent {
  c?: IC;

  constructor(protected cService: CService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
