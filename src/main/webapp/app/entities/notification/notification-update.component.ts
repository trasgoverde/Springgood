import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { INotification, Notification } from 'app/shared/model/notification.model';
import { NotificationService } from './notification.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    notificationDate: [],
    notificationReason: [null, [Validators.required]],
    notificationText: [null, [Validators.minLength(2), Validators.maxLength(100)]],
    isDelivered: [],
  });

  constructor(protected notificationService: NotificationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (!notification.id) {
        const today = moment().startOf('day');
        notification.creationDate = today;
        notification.notificationDate = today;
      }

      this.updateForm(notification);
    });
  }

  updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      creationDate: notification.creationDate ? notification.creationDate.format(DATE_TIME_FORMAT) : null,
      notificationDate: notification.notificationDate ? notification.notificationDate.format(DATE_TIME_FORMAT) : null,
      notificationReason: notification.notificationReason,
      notificationText: notification.notificationText,
      isDelivered: notification.isDelivered,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  private createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      notificationDate: this.editForm.get(['notificationDate'])!.value
        ? moment(this.editForm.get(['notificationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      notificationReason: this.editForm.get(['notificationReason'])!.value,
      notificationText: this.editForm.get(['notificationText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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
