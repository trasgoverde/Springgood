import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVquestion, Vquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from './vquestion.service';
import { IVtopic } from 'app/shared/model/vtopic.model';
import { VtopicService } from 'app/entities/vtopic/vtopic.service';

@Component({
  selector: 'jhi-vquestion-update',
  templateUrl: './vquestion-update.component.html',
})
export class VquestionUpdateComponent implements OnInit {
  isSaving = false;
  vtopics: IVtopic[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    vquestion: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    vquestionDescription: [null, [Validators.minLength(2), Validators.maxLength(250)]],
    vtopic: [null, Validators.required],
  });

  constructor(
    protected vquestionService: VquestionService,
    protected vtopicService: VtopicService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vquestion }) => {
      if (!vquestion.id) {
        const today = moment().startOf('day');
        vquestion.creationDate = today;
      }

      this.updateForm(vquestion);

      this.vtopicService.query().subscribe((res: HttpResponse<IVtopic[]>) => (this.vtopics = res.body || []));
    });
  }

  updateForm(vquestion: IVquestion): void {
    this.editForm.patchValue({
      id: vquestion.id,
      creationDate: vquestion.creationDate ? vquestion.creationDate.format(DATE_TIME_FORMAT) : null,
      vquestion: vquestion.vquestion,
      vquestionDescription: vquestion.vquestionDescription,
      vtopic: vquestion.vtopic,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vquestion = this.createFromForm();
    if (vquestion.id !== undefined) {
      this.subscribeToSaveResponse(this.vquestionService.update(vquestion));
    } else {
      this.subscribeToSaveResponse(this.vquestionService.create(vquestion));
    }
  }

  private createFromForm(): IVquestion {
    return {
      ...new Vquestion(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vquestion: this.editForm.get(['vquestion'])!.value,
      vquestionDescription: this.editForm.get(['vquestionDescription'])!.value,
      vtopic: this.editForm.get(['vtopic'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVquestion>>): void {
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

  trackById(index: number, item: IVtopic): any {
    return item.id;
  }
}
