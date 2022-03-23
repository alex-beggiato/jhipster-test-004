import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IB } from '../b.model';
import { BService } from '../service/b.service';
import { BDeleteDialogComponent } from '../delete/b-delete-dialog.component';

@Component({
  selector: 'jhi-b',
  templateUrl: './b.component.html',
})
export class BComponent implements OnInit {
  bS?: IB[];
  isLoading = false;

  constructor(protected bService: BService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bService.query().subscribe({
      next: (res: HttpResponse<IB[]>) => {
        this.isLoading = false;
        this.bS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IB): number {
    return item.id!;
  }

  delete(b: IB): void {
    const modalRef = this.modalService.open(BDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.b = b;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
