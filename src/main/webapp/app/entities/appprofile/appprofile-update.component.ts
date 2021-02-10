import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAppprofile, Appprofile } from 'app/shared/model/appprofile.model';
import { AppprofileService } from './appprofile.service';
import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from 'app/entities/appuser/appuser.service';

@Component({
  selector: 'jhi-appprofile-update',
  templateUrl: './appprofile-update.component.html',
})
export class AppprofileUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    gender: [],
    phone: [null, [Validators.maxLength(20)]],
    bio: [null, [Validators.maxLength(7500)]],
    facebook: [null, [Validators.maxLength(50)]],
    twitter: [null, [Validators.maxLength(50)]],
    linkedin: [null, [Validators.maxLength(50)]],
    instagram: [null, [Validators.maxLength(50)]],
    googlePlus: [null, [Validators.maxLength(50)]],
    birthdate: [],
    sibblings: [null, [Validators.min(-1), Validators.max(20)]],
    appuser: [null, Validators.required],
  });

  constructor(
    protected appprofileService: AppprofileService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appprofile }) => {
      if (!appprofile.id) {
        const today = moment().startOf('day');
        appprofile.creationDate = today;
        appprofile.birthdate = today;
      }

      this.updateForm(appprofile);

      this.appuserService
        .query({ filter: 'appprofile-is-null' })
        .pipe(
          map((res: HttpResponse<IAppuser[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAppuser[]) => {
          if (!appprofile.appuser || !appprofile.appuser.id) {
            this.appusers = resBody;
          } else {
            this.appuserService
              .find(appprofile.appuser.id)
              .pipe(
                map((subRes: HttpResponse<IAppuser>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAppuser[]) => (this.appusers = concatRes));
          }
        });
    });
  }

  updateForm(appprofile: IAppprofile): void {
    this.editForm.patchValue({
      id: appprofile.id,
      creationDate: appprofile.creationDate ? appprofile.creationDate.format(DATE_TIME_FORMAT) : null,
      gender: appprofile.gender,
      phone: appprofile.phone,
      bio: appprofile.bio,
      facebook: appprofile.facebook,
      twitter: appprofile.twitter,
      linkedin: appprofile.linkedin,
      instagram: appprofile.instagram,
      googlePlus: appprofile.googlePlus,
      birthdate: appprofile.birthdate ? appprofile.birthdate.format(DATE_TIME_FORMAT) : null,
      sibblings: appprofile.sibblings,
      appuser: appprofile.appuser,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appprofile = this.createFromForm();
    if (appprofile.id !== undefined) {
      this.subscribeToSaveResponse(this.appprofileService.update(appprofile));
    } else {
      this.subscribeToSaveResponse(this.appprofileService.create(appprofile));
    }
  }

  private createFromForm(): IAppprofile {
    return {
      ...new Appprofile(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      gender: this.editForm.get(['gender'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      bio: this.editForm.get(['bio'])!.value,
      facebook: this.editForm.get(['facebook'])!.value,
      twitter: this.editForm.get(['twitter'])!.value,
      linkedin: this.editForm.get(['linkedin'])!.value,
      instagram: this.editForm.get(['instagram'])!.value,
      googlePlus: this.editForm.get(['googlePlus'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value ? moment(this.editForm.get(['birthdate'])!.value, DATE_TIME_FORMAT) : undefined,
      sibblings: this.editForm.get(['sibblings'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppprofile>>): void {
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
}
