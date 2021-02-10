import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICalbum, Calbum } from 'app/shared/model/calbum.model';
import { CalbumService } from './calbum.service';

@Component({
  selector: 'jhi-calbum-update',
  templateUrl: './calbum-update.component.html',
})
export class CalbumUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
  });

  constructor(protected calbumService: CalbumService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calbum }) => {
      if (!calbum.id) {
        const today = moment().startOf('day');
        calbum.creationDate = today;
      }

      this.updateForm(calbum);
    });
  }

  updateForm(calbum: ICalbum): void {
    this.editForm.patchValue({
      id: calbum.id,
      creationDate: calbum.creationDate ? calbum.creationDate.format(DATE_TIME_FORMAT) : null,
      title: calbum.title,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calbum = this.createFromForm();
    if (calbum.id !== undefined) {
      this.subscribeToSaveResponse(this.calbumService.update(calbum));
    } else {
      this.subscribeToSaveResponse(this.calbumService.create(calbum));
    }
  }

  private createFromForm(): ICalbum {
    return {
      ...new Calbum(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalbum>>): void {
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
