import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICommunity, Community } from 'app/shared/model/community.model';
import { CommunityService } from './community.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ICalbum } from 'app/shared/model/calbum.model';
import { CalbumService } from 'app/entities/calbum/calbum.service';

@Component({
  selector: 'jhi-community-update',
  templateUrl: './community-update.component.html',
})
export class CommunityUpdateComponent implements OnInit {
  isSaving = false;
  calbums: ICalbum[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    communityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    communityDescription: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(7500)]],
    image: [],
    imageContentType: [],
    isActive: [],
    calbum: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected communityService: CommunityService,
    protected calbumService: CalbumService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ community }) => {
      if (!community.id) {
        const today = moment().startOf('day');
        community.creationDate = today;
      }

      this.updateForm(community);

      this.calbumService.query().subscribe((res: HttpResponse<ICalbum[]>) => (this.calbums = res.body || []));
    });
  }

  updateForm(community: ICommunity): void {
    this.editForm.patchValue({
      id: community.id,
      creationDate: community.creationDate ? community.creationDate.format(DATE_TIME_FORMAT) : null,
      communityName: community.communityName,
      communityDescription: community.communityDescription,
      image: community.image,
      imageContentType: community.imageContentType,
      isActive: community.isActive,
      calbum: community.calbum,
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
    const community = this.createFromForm();
    if (community.id !== undefined) {
      this.subscribeToSaveResponse(this.communityService.update(community));
    } else {
      this.subscribeToSaveResponse(this.communityService.create(community));
    }
  }

  private createFromForm(): ICommunity {
    return {
      ...new Community(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      communityName: this.editForm.get(['communityName'])!.value,
      communityDescription: this.editForm.get(['communityDescription'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      calbum: this.editForm.get(['calbum'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunity>>): void {
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

  trackById(index: number, item: ICalbum): any {
    return item.id;
  }
}
