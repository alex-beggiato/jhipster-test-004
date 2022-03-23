import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IB, B } from '../b.model';
import { BService } from '../service/b.service';
import { IA } from 'app/entities/a/a.model';
import { AService } from 'app/entities/a/service/a.service';

@Component({
  selector: 'jhi-b-update',
  templateUrl: './b-update.component.html',
})
export class BUpdateComponent implements OnInit {
  isSaving = false;

  aSSharedCollection: IA[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [],
    entAAAuid: [],
    entBBBcod: [],
    entBBBdsc: [],
    entAAAuid: [],
  });

  constructor(
    protected bService: BService,
    protected aService: AService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ b }) => {
      this.updateForm(b);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const b = this.createFromForm();
    if (b.id !== undefined) {
      this.subscribeToSaveResponse(this.bService.update(b));
    } else {
      this.subscribeToSaveResponse(this.bService.create(b));
    }
  }

  trackAById(index: number, item: IA): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IB>>): void {
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

  protected updateForm(b: IB): void {
    this.editForm.patchValue({
      id: b.id,
      uid: b.uid,
      entAAAuid: b.entAAAuid,
      entBBBcod: b.entBBBcod,
      entBBBdsc: b.entBBBdsc,
      entAAAuid: b.entAAAuid,
    });

    this.aSSharedCollection = this.aService.addAToCollectionIfMissing(this.aSSharedCollection, b.entAAAuid);
  }

  protected loadRelationshipsOptions(): void {
    this.aService
      .query()
      .pipe(map((res: HttpResponse<IA[]>) => res.body ?? []))
      .pipe(map((aS: IA[]) => this.aService.addAToCollectionIfMissing(aS, this.editForm.get('entAAAuid')!.value)))
      .subscribe((aS: IA[]) => (this.aSSharedCollection = aS));
  }

  protected createFromForm(): IB {
    return {
      ...new B(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      entAAAuid: this.editForm.get(['entAAAuid'])!.value,
      entBBBcod: this.editForm.get(['entBBBcod'])!.value,
      entBBBdsc: this.editForm.get(['entBBBdsc'])!.value,
      entAAAuid: this.editForm.get(['entAAAuid'])!.value,
    };
  }
}
