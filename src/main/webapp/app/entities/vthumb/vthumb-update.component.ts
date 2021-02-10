import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVthumb, Vthumb } from 'app/shared/model/vthumb.model';
import { VthumbService } from './vthumb.service';
import { IVquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from 'app/entities/vquestion/vquestion.service';
import { IVanswer } from 'app/shared/model/vanswer.model';
import { VanswerService } from 'app/entities/vanswer/vanswer.service';

type SelectableEntity = IVquestion | IVanswer;

@Component({
  selector: 'jhi-vthumb-update',
  templateUrl: './vthumb-update.component.html',
})
export class VthumbUpdateComponent implements OnInit {
  isSaving = false;
  vquestions: IVquestion[] = [];
  vanswers: IVanswer[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    vthumbUp: [],
    vthumbDown: [],
    vquestion: [],
    vanswer: [],
  });

  constructor(
    protected vthumbService: VthumbService,
    protected vquestionService: VquestionService,
    protected vanswerService: VanswerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vthumb }) => {
      if (!vthumb.id) {
        const today = moment().startOf('day');
        vthumb.creationDate = today;
      }

      this.updateForm(vthumb);

      this.vquestionService.query().subscribe((res: HttpResponse<IVquestion[]>) => (this.vquestions = res.body || []));

      this.vanswerService.query().subscribe((res: HttpResponse<IVanswer[]>) => (this.vanswers = res.body || []));
    });
  }

  updateForm(vthumb: IVthumb): void {
    this.editForm.patchValue({
      id: vthumb.id,
      creationDate: vthumb.creationDate ? vthumb.creationDate.format(DATE_TIME_FORMAT) : null,
      vthumbUp: vthumb.vthumbUp,
      vthumbDown: vthumb.vthumbDown,
      vquestion: vthumb.vquestion,
      vanswer: vthumb.vanswer,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vthumb = this.createFromForm();
    if (vthumb.id !== undefined) {
      this.subscribeToSaveResponse(this.vthumbService.update(vthumb));
    } else {
      this.subscribeToSaveResponse(this.vthumbService.create(vthumb));
    }
  }

  private createFromForm(): IVthumb {
    return {
      ...new Vthumb(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vthumbUp: this.editForm.get(['vthumbUp'])!.value,
      vthumbDown: this.editForm.get(['vthumbDown'])!.value,
      vquestion: this.editForm.get(['vquestion'])!.value,
      vanswer: this.editForm.get(['vanswer'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVthumb>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
