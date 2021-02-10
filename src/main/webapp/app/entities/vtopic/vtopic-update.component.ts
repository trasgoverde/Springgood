import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVtopic, Vtopic } from 'app/shared/model/vtopic.model';
import { VtopicService } from './vtopic.service';

@Component({
  selector: 'jhi-vtopic-update',
  templateUrl: './vtopic-update.component.html',
})
export class VtopicUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    vtopicTitle: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    vtopicDescription: [null, [Validators.minLength(2), Validators.maxLength(250)]],
  });

  constructor(protected vtopicService: VtopicService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vtopic }) => {
      if (!vtopic.id) {
        const today = moment().startOf('day');
        vtopic.creationDate = today;
      }

      this.updateForm(vtopic);
    });
  }

  updateForm(vtopic: IVtopic): void {
    this.editForm.patchValue({
      id: vtopic.id,
      creationDate: vtopic.creationDate ? vtopic.creationDate.format(DATE_TIME_FORMAT) : null,
      vtopicTitle: vtopic.vtopicTitle,
      vtopicDescription: vtopic.vtopicDescription,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vtopic = this.createFromForm();
    if (vtopic.id !== undefined) {
      this.subscribeToSaveResponse(this.vtopicService.update(vtopic));
    } else {
      this.subscribeToSaveResponse(this.vtopicService.create(vtopic));
    }
  }

  private createFromForm(): IVtopic {
    return {
      ...new Vtopic(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vtopicTitle: this.editForm.get(['vtopicTitle'])!.value,
      vtopicDescription: this.editForm.get(['vtopicDescription'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVtopic>>): void {
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
