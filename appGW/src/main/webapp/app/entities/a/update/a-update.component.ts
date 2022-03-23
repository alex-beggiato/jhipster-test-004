import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IA, A } from '../a.model';
import { AService } from '../service/a.service';

@Component({
  selector: 'jhi-a-update',
  templateUrl: './a-update.component.html',
})
export class AUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [],
    entAAAcod: [],
    entAAAdsc: [],
  });

  constructor(protected aService: AService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ a }) => {
      this.updateForm(a);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const a = this.createFromForm();
    if (a.id !== undefined) {
      this.subscribeToSaveResponse(this.aService.update(a));
    } else {
      this.subscribeToSaveResponse(this.aService.create(a));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IA>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(a: IA): void {
    this.editForm.patchValue({
      id: a.id,
      uid: a.uid,
      entAAAcod: a.entAAAcod,
      entAAAdsc: a.entAAAdsc,
    });
  }

  protected createFromForm(): IA {
    return {
      ...new A(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      entAAAcod: this.editForm.get(['entAAAcod'])!.value,
      entAAAdsc: this.editForm.get(['entAAAdsc'])!.value,
    };
  }
}
