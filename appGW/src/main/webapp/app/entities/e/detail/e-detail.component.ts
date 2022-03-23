import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IE } from '../e.model';

@Component({
  selector: 'jhi-e-detail',
  templateUrl: './e-detail.component.html',
})
export class EDetailComponent implements OnInit {
  e: IE | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ e }) => {
      this.e = e;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
