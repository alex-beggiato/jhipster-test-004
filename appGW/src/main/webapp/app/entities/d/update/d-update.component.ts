import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ID, D } from '../d.model';
import { DService } from '../service/d.service';

@Component({
  selector: 'jhi-d-update',
  templateUrl: './d-update.component.html',
})
export class DUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [],
    entDDDcod: [],
    entDDDdsc: [],
  });

  constructor(protected dService: DService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ d }) => {
      this.updateForm(d);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const d = this.createFromForm();
    if (d.id !== undefined) {
      this.subscribeToSaveResponse(this.dService.update(d));
    } else {
      this.subscribeToSaveResponse(this.dService.create(d));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ID>>): void {
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

  protected updateForm(d: ID): void {
    this.editForm.patchValue({
      id: d.id,
      uid: d.uid,
      entDDDcod: d.entDDDcod,
      entDDDdsc: d.entDDDdsc,
    });
  }

  protected createFromForm(): ID {
    return {
      ...new D(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      entDDDcod: this.editForm.get(['entDDDcod'])!.value,
      entDDDdsc: this.editForm.get(['entDDDdsc'])!.value,
    };
  }
}
