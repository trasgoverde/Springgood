import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICceleb, Cceleb } from 'app/shared/model/cceleb.model';
import { CcelebService } from './cceleb.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-cceleb-update',
  templateUrl: './cceleb-update.component.html',
})
export class CcelebUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    celebName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected ccelebService: CcelebService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cceleb }) => {
      this.updateForm(cceleb);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(cceleb: ICceleb): void {
    this.editForm.patchValue({
      id: cceleb.id,
      celebName: cceleb.celebName,
      communities: cceleb.communities,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cceleb = this.createFromForm();
    if (cceleb.id !== undefined) {
      this.subscribeToSaveResponse(this.ccelebService.update(cceleb));
    } else {
      this.subscribeToSaveResponse(this.ccelebService.create(cceleb));
    }
  }

  private createFromForm(): ICceleb {
    return {
      ...new Cceleb(),
      id: this.editForm.get(['id'])!.value,
      celebName: this.editForm.get(['celebName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICceleb>>): void {
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

  trackById(index: number, item: ICommunity): any {
    return item.id;
  }

  getSelected(selectedVals: ICommunity[], option: ICommunity): ICommunity {
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
