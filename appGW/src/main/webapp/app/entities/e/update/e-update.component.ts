import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IE, E } from '../e.model';
import { EService } from '../service/e.service';
import { IC } from 'app/entities/c/c.model';
import { CService } from 'app/entities/c/service/c.service';

@Component({
  selector: 'jhi-e-update',
  templateUrl: './e-update.component.html',
})
export class EUpdateComponent implements OnInit {
  isSaving = false;

  cSSharedCollection: IC[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [],
    entCCCuid: [],
    entEEEcod: [],
    entEEEdsc: [],
    entCCCuid: [],
  });

  constructor(
    protected eService: EService,
    protected cService: CService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ e }) => {
      this.updateForm(e);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const e = this.createFromForm();
    if (e.id !== undefined) {
      this.subscribeToSaveResponse(this.eService.update(e));
    } else {
      this.subscribeToSaveResponse(this.eService.create(e));
    }
  }

  trackCById(index: number, item: IC): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IE>>): void {
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

  protected updateForm(e: IE): void {
    this.editForm.patchValue({
      id: e.id,
      uid: e.uid,
      entCCCuid: e.entCCCuid,
      entEEEcod: e.entEEEcod,
      entEEEdsc: e.entEEEdsc,
      entCCCuid: e.entCCCuid,
    });

    this.cSSharedCollection = this.cService.addCToCollectionIfMissing(this.cSSharedCollection, e.entCCCuid);
  }

  protected loadRelationshipsOptions(): void {
    this.cService
      .query()
      .pipe(map((res: HttpResponse<IC[]>) => res.body ?? []))
      .pipe(map((cS: IC[]) => this.cService.addCToCollectionIfMissing(cS, this.editForm.get('entCCCuid')!.value)))
      .subscribe((cS: IC[]) => (this.cSSharedCollection = cS));
  }

  protected createFromForm(): IE {
    return {
      ...new E(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      entCCCuid: this.editForm.get(['entCCCuid'])!.value,
      entEEEcod: this.editForm.get(['entEEEcod'])!.value,
      entEEEdsc: this.editForm.get(['entEEEdsc'])!.value,
      entCCCuid: this.editForm.get(['entCCCuid'])!.value,
    };
  }
}
