import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IA } from '../a.model';
import { AService } from '../service/a.service';
import { ADeleteDialogComponent } from '../delete/a-delete-dialog.component';

@Component({
  selector: 'jhi-a',
  templateUrl: './a.component.html',
})
export class AComponent implements OnInit {
  aS?: IA[];
  isLoading = false;

  constructor(protected aService: AService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.aService.query().subscribe({
      next: (res: HttpResponse<IA[]>) => {
        this.isLoading = false;
        this.aS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IA): number {
    return item.id!;
  }

  delete(a: IA): void {
    const modalRef = this.modalService.open(ADeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.a = a;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
