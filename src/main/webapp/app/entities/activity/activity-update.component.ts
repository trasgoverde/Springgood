import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IActivity, Activity } from 'app/shared/model/activity.model';
import { ActivityService } from './activity.service';
import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from 'app/entities/appuser/appuser.service';

@Component({
  selector: 'jhi-activity-update',
  templateUrl: './activity-update.component.html',
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    activityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected activityService: ActivityService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.updateForm(activity);

      this.appuserService.query().subscribe((res: HttpResponse<IAppuser[]>) => (this.appusers = res.body || []));
    });
  }

  updateForm(activity: IActivity): void {
    this.editForm.patchValue({
      id: activity.id,
      activityName: activity.activityName,
      appusers: activity.appusers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.createFromForm();
    if (activity.id !== undefined) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
    }
  }

  private createFromForm(): IActivity {
    return {
      ...new Activity(),
      id: this.editForm.get(['id'])!.value,
      activityName: this.editForm.get(['activityName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivity>>): void {
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

  trackById(index: number, item: IAppuser): any {
    return item.id;
  }

  getSelected(selectedVals: IAppuser[], option: IAppuser): IAppuser {
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
