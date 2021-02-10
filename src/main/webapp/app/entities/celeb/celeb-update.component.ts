import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICeleb, Celeb } from 'app/shared/model/celeb.model';
import { CelebService } from './celeb.service';
import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from 'app/entities/appuser/appuser.service';

@Component({
  selector: 'jhi-celeb-update',
  templateUrl: './celeb-update.component.html',
})
export class CelebUpdateComponent implements OnInit {
  isSaving = false;
  appusers: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    celebName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected celebService: CelebService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ celeb }) => {
      this.updateForm(celeb);

      this.appuserService.query().subscribe((res: HttpResponse<IAppuser[]>) => (this.appusers = res.body || []));
    });
  }

  updateForm(celeb: ICeleb): void {
    this.editForm.patchValue({
      id: celeb.id,
      celebName: celeb.celebName,
      appusers: celeb.appusers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const celeb = this.createFromForm();
    if (celeb.id !== undefined) {
      this.subscribeToSaveResponse(this.celebService.update(celeb));
    } else {
      this.subscribeToSaveResponse(this.celebService.create(celeb));
    }
  }

  private createFromForm(): ICeleb {
    return {
      ...new Celeb(),
      id: this.editForm.get(['id'])!.value,
      celebName: this.editForm.get(['celebName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICeleb>>): void {
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
