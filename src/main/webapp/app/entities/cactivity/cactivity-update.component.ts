import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICactivity, Cactivity } from 'app/shared/model/cactivity.model';
import { CactivityService } from './cactivity.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-cactivity-update',
  templateUrl: './cactivity-update.component.html',
})
export class CactivityUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    activityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected cactivityService: CactivityService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cactivity }) => {
      this.updateForm(cactivity);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(cactivity: ICactivity): void {
    this.editForm.patchValue({
      id: cactivity.id,
      activityName: cactivity.activityName,
      communities: cactivity.communities,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cactivity = this.createFromForm();
    if (cactivity.id !== undefined) {
      this.subscribeToSaveResponse(this.cactivityService.update(cactivity));
    } else {
      this.subscribeToSaveResponse(this.cactivityService.create(cactivity));
    }
  }

  private createFromForm(): ICactivity {
    return {
      ...new Cactivity(),
      id: this.editForm.get(['id'])!.value,
      activityName: this.editForm.get(['activityName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICactivity>>): void {
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
