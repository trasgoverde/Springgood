import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBmessage, Bmessage } from 'app/shared/model/bmessage.model';
import { BmessageService } from './bmessage.service';

@Component({
  selector: 'jhi-bmessage-update',
  templateUrl: './bmessage-update.component.html',
})
export class BmessageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    bmessageText: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(8000)]],
    isDelivered: [],
  });

  constructor(protected bmessageService: BmessageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bmessage }) => {
      if (!bmessage.id) {
        const today = moment().startOf('day');
        bmessage.creationDate = today;
      }

      this.updateForm(bmessage);
    });
  }

  updateForm(bmessage: IBmessage): void {
    this.editForm.patchValue({
      id: bmessage.id,
      creationDate: bmessage.creationDate ? bmessage.creationDate.format(DATE_TIME_FORMAT) : null,
      bmessageText: bmessage.bmessageText,
      isDelivered: bmessage.isDelivered,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bmessage = this.createFromForm();
    if (bmessage.id !== undefined) {
      this.subscribeToSaveResponse(this.bmessageService.update(bmessage));
    } else {
      this.subscribeToSaveResponse(this.bmessageService.create(bmessage));
    }
  }

  private createFromForm(): IBmessage {
    return {
      ...new Bmessage(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      bmessageText: this.editForm.get(['bmessageText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBmessage>>): void {
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
}
