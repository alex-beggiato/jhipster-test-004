import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IC } from '../c.model';
import { CService } from '../service/c.service';
import { CDeleteDialogComponent } from '../delete/c-delete-dialog.component';

@Component({
  selector: 'jhi-c',
  templateUrl: './c.component.html',
})
export class CComponent implements OnInit {
  cS?: IC[];
  isLoading = false;

  constructor(protected cService: CService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cService.query().subscribe({
      next: (res: HttpResponse<IC[]>) => {
        this.isLoading = false;
        this.cS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IC): number {
    return item.id!;
  }

  delete(c: IC): void {
    const modalRef = this.modalService.open(CDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.c = c;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
