import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICinterest, Cinterest } from 'app/shared/model/cinterest.model';
import { CinterestService } from './cinterest.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-cinterest-update',
  templateUrl: './cinterest-update.component.html',
})
export class CinterestUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    interestName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected cinterestService: CinterestService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cinterest }) => {
      this.updateForm(cinterest);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(cinterest: ICinterest): void {
    this.editForm.patchValue({
      id: cinterest.id,
      interestName: cinterest.interestName,
      communities: cinterest.communities,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cinterest = this.createFromForm();
    if (cinterest.id !== undefined) {
      this.subscribeToSaveResponse(this.cinterestService.update(cinterest));
    } else {
      this.subscribeToSaveResponse(this.cinterestService.create(cinterest));
    }
  }

  private createFromForm(): ICinterest {
    return {
      ...new Cinterest(),
      id: this.editForm.get(['id'])!.value,
      interestName: this.editForm.get(['interestName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICinterest>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICommunity): any {
    return item.id;
  }

  getSelected(selectedVals: ICommunity[], option: ICommunity): ICommunity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
