import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IAppphoto, Appphoto } from 'app/shared/model/appphoto.model';
import { AppphotoService } from './appphoto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from 'app/entities/appuser/appuser.service';

@Component({
  selector: 'jhi-appphoto-update',
  templateUrl: './appphoto-update.component.html',
})
export class AppphotoUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    appuser: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected appphotoService: AppphotoService,
    protected appuserService: AppuserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appphoto }) => {
      if (!appphoto.id) {
        const today = moment().startOf('day');
        appphoto.creationDate = today;
      }

      this.updateForm(appphoto);

      this.appuserService
        .query({ filter: 'appphoto-is-null' })
        .pipe(
          map((res: HttpResponse<IAppuser[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAppuser[]) => {
          if (!appphoto.appuser || !appphoto.appuser.id) {
            this.appusers = resBody;
          } else {
            this.appuserService
              .find(appphoto.appuser.id)
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

  updateForm(appphoto: IAppphoto): void {
    this.editForm.patchValue({
      id: appphoto.id,
      creationDate: appphoto.creationDate ? appphoto.creationDate.format(DATE_TIME_FORMAT) : null,
      image: appphoto.image,
      imageContentType: appphoto.imageContentType,
      appuser: appphoto.appuser,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('springgoodApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appphoto = this.createFromForm();
    if (appphoto.id !== undefined) {
      this.subscribeToSaveResponse(this.appphotoService.update(appphoto));
    } else {
      this.subscribeToSaveResponse(this.appphotoService.create(appphoto));
    }
  }

  private createFromForm(): IAppphoto {
    return {
      ...new Appphoto(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppphoto>>): void {
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
