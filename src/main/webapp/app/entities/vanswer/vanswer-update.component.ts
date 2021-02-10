import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVanswer, Vanswer } from 'app/shared/model/vanswer.model';
import { VanswerService } from './vanswer.service';
import { IVquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/vquestion.service';

@Component({
  selector: 'jhi-vanswer-update',
  templateUrl: './vanswer-update.component.html',
})
export class VanswerUpdateComponent implements OnInit {
  isSaving = false;
  vquestions: IVquestion[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    urlVanswer: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(500)]],
    accepted: [],
    vquestion: [null, Validators.required],
  });

  constructor(
    protected vanswerService: VanswerService,
    protected vquestionService: VquestionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vanswer }) => {
      if (!vanswer.id) {
        const today = moment().startOf('day');
        vanswer.creationDate = today;
      }

      this.updateForm(vanswer);

      this.vquestionService.query().subscribe((res: HttpResponse<IVquestion[]>) => (this.vquestions = res.body || []));
    });
  }

  updateForm(vanswer: IVanswer): void {
    this.editForm.patchValue({
      id: vanswer.id,
      creationDate: vanswer.creationDate ? vanswer.creationDate.format(DATE_TIME_FORMAT) : null,
      urlVanswer: vanswer.urlVanswer,
      accepted: vanswer.accepted,
      vquestion: vanswer.vquestion,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vanswer = this.createFromForm();
    if (vanswer.id !== undefined) {
      this.subscribeToSaveResponse(this.vanswerService.update(vanswer));
    } else {
      this.subscribeToSaveResponse(this.vanswerService.create(vanswer));
    }
  }

  private createFromForm(): IVanswer {
    return {
      ...new Vanswer(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      urlVanswer: this.editForm.get(['urlVanswer'])!.value,
      accepted: this.editForm.get(['accepted'])!.value,
      vquestion: this.editForm.get(['vquestion'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVanswer>>): void {
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

  trackById(index: number, item: IVquestion): any {
    return item.id;
  }
}
