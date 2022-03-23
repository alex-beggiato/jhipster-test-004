import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ID } from '../d.model';
import { DService } from '../service/d.service';
import { DDeleteDialogComponent } from '../delete/d-delete-dialog.component';

@Component({
  selector: 'jhi-d',
  templateUrl: './d.component.html',
})
export class DComponent implements OnInit {
  dS?: ID[];
  isLoading = false;

  constructor(protected dService: DService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dService.query().subscribe({
      next: (res: HttpResponse<ID[]>) => {
        this.isLoading = false;
        this.dS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ID): number {
    return item.id!;
  }

  delete(d: ID): void {
    const modalRef = this.modalService.open(DDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.d = d;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
