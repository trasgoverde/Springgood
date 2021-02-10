import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICmessage, Cmessage } from 'app/shared/model/cmessage.model';
import { CmessageService } from './cmessage.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-cmessage-update',
  templateUrl: './cmessage-update.component.html',
})
export class CmessageUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    cmessageText: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(8000)]],
    isDelivered: [],
    csender: [],
    creceiver: [],
  });

  constructor(
    protected cmessageService: CmessageService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmessage }) => {
      if (!cmessage.id) {
        const today = moment().startOf('day');
        cmessage.creationDate = today;
      }

      this.updateForm(cmessage);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(cmessage: ICmessage): void {
    this.editForm.patchValue({
      id: cmessage.id,
      creationDate: cmessage.creationDate ? cmessage.creationDate.format(DATE_TIME_FORMAT) : null,
      cmessageText: cmessage.cmessageText,
      isDelivered: cmessage.isDelivered,
      csender: cmessage.csender,
      creceiver: cmessage.creceiver,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cmessage = this.createFromForm();
    if (cmessage.id !== undefined) {
      this.subscribeToSaveResponse(this.cmessageService.update(cmessage));
    } else {
      this.subscribeToSaveResponse(this.cmessageService.create(cmessage));
    }
  }

  private createFromForm(): ICmessage {
    return {
      ...new Cmessage(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cmessageText: this.editForm.get(['cmessageText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
      csender: this.editForm.get(['csender'])!.value,
      creceiver: this.editForm.get(['creceiver'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICmessage>>): void {
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
