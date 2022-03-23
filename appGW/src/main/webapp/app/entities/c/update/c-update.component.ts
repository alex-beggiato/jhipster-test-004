import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IC, C } from '../c.model';
import { CService } from '../service/c.service';

@Component({
  selector: 'jhi-c-update',
  templateUrl: './c-update.component.html',
})
export class CUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [],
    entCCCcod: [],
    entCCCdsc: [],
  });

  constructor(protected cService: CService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ c }) => {
      this.updateForm(c);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const c = this.createFromForm();
    if (c.id !== undefined) {
      this.subscribeToSaveResponse(this.cService.update(c));
    } else {
      this.subscribeToSaveResponse(this.cService.create(c));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IC>>): void {
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

  protected updateForm(c: IC): void {
    this.editForm.patchValue({
      id: c.id,
      uid: c.uid,
      entCCCcod: c.entCCCcod,
      entCCCdsc: c.entCCCdsc,
    });
  }

  protected createFromForm(): IC {
    return {
      ...new C(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      entCCCcod: this.editForm.get(['entCCCcod'])!.value,
      entCCCdsc: this.editForm.get(['entCCCdsc'])!.value,
    };
  }
}
