import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IE } from '../e.model';
import { EService } from '../service/e.service';
import { EDeleteDialogComponent } from '../delete/e-delete-dialog.component';

@Component({
  selector: 'jhi-e',
  templateUrl: './e.component.html',
})
export class EComponent implements OnInit {
  eS?: IE[];
  isLoading = false;

  constructor(protected eService: EService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eService.query().subscribe({
      next: (res: HttpResponse<IE[]>) => {
        this.isLoading = false;
        this.eS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IE): number {
    return item.id!;
  }

  delete(e: IE): void {
    const modalRef = this.modalService.open(EDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.e = e;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
