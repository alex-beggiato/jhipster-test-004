import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IA } from '../a.model';

@Component({
  selector: 'jhi-a-detail',
  templateUrl: './a-detail.component.html',
})
export class ADetailComponent implements OnInit {
  a: IA | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ a }) => {
      this.a = a;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
