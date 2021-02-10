import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFollow, Follow } from 'app/shared/model/follow.model';
import { FollowService } from './follow.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-follow-update',
  templateUrl: './follow-update.component.html',
})
export class FollowUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    cfollowed: [],
    cfollowing: [],
  });

  constructor(
    protected followService: FollowService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ follow }) => {
      if (!follow.id) {
        const today = moment().startOf('day');
        follow.creationDate = today;
      }

      this.updateForm(follow);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(follow: IFollow): void {
    this.editForm.patchValue({
      id: follow.id,
      creationDate: follow.creationDate ? follow.creationDate.format(DATE_TIME_FORMAT) : null,
      cfollowed: follow.cfollowed,
      cfollowing: follow.cfollowing,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const follow = this.createFromForm();
    if (follow.id !== undefined) {
      this.subscribeToSaveResponse(this.followService.update(follow));
    } else {
      this.subscribeToSaveResponse(this.followService.create(follow));
    }
  }

  private createFromForm(): IFollow {
    return {
      ...new Follow(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cfollowed: this.editForm.get(['cfollowed'])!.value,
      cfollowing: this.editForm.get(['cfollowing'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFollow>>): void {
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
}
