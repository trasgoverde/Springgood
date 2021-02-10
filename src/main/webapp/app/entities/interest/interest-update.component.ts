import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInterest, Interest } from 'app/shared/model/interest.model';
import { InterestService } from './interest.service';
import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from 'app/entities/appuser/appuser.service';

@Component({
  selector: 'jhi-interest-update',
  templateUrl: './interest-update.component.html',
})
export class InterestUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    interestName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected interestService: InterestService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interest }) => {
      this.updateForm(interest);

      this.appuserService.query().subscribe((res: HttpResponse<IAppuser[]>) => (this.appusers = res.body || []));
    });
  }

  updateForm(interest: IInterest): void {
    this.editForm.patchValue({
      id: interest.id,
      interestName: interest.interestName,
      appusers: interest.appusers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interest = this.createFromForm();
    if (interest.id !== undefined) {
      this.subscribeToSaveResponse(this.interestService.update(interest));
    } else {
      this.subscribeToSaveResponse(this.interestService.create(interest));
    }
  }

  private createFromForm(): IInterest {
    return {
      ...new Interest(),
      id: this.editForm.get(['id'])!.value,
      interestName: this.editForm.get(['interestName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterest>>): void {
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

  getSelected(selectedVals: IAppuser[], option: IAppuser): IAppuser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
