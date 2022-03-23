import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IC } from '../c.model';

@Component({
  selector: 'jhi-c-detail',
  templateUrl: './c-detail.component.html',
})
export class CDetailComponent implements OnInit {
  c: IC | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ c }) => {
      this.c = c;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
