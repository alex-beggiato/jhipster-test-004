import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ID } from '../d.model';

@Component({
  selector: 'jhi-d-detail',
  templateUrl: './d-detail.component.html',
})
export class DDetailComponent implements OnInit {
  d: ID | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ d }) => {
      this.d = d;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
