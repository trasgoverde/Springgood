import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBlockuser, Blockuser } from 'app/shared/model/blockuser.model';
import { BlockuserService } from './blockuser.service';
import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from 'app/entities/community/community.service';

@Component({
  selector: 'jhi-blockuser-update',
  templateUrl: './blockuser-update.component.html',
})
export class BlockuserUpdateComponent implements OnInit {
  isSaving = false;
  communities: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    cblockeduser: [],
    cblockinguser: [],
  });

  constructor(
    protected blockuserService: BlockuserService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blockuser }) => {
      if (!blockuser.id) {
        const today = moment().startOf('day');
        blockuser.creationDate = today;
      }

      this.updateForm(blockuser);

      this.communityService.query().subscribe((res: HttpResponse<ICommunity[]>) => (this.communities = res.body || []));
    });
  }

  updateForm(blockuser: IBlockuser): void {
    this.editForm.patchValue({
      id: blockuser.id,
      creationDate: blockuser.creationDate ? blockuser.creationDate.format(DATE_TIME_FORMAT) : null,
      cblockeduser: blockuser.cblockeduser,
      cblockinguser: blockuser.cblockinguser,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blockuser = this.createFromForm();
    if (blockuser.id !== undefined) {
      this.subscribeToSaveResponse(this.blockuserService.update(blockuser));
    } else {
      this.subscribeToSaveResponse(this.blockuserService.create(blockuser));
    }
  }

  private createFromForm(): IBlockuser {
    return {
      ...new Blockuser(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      cblockeduser: this.editForm.get(['cblockeduser'])!.value,
      cblockinguser: this.editForm.get(['cblockinguser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlockuser>>): void {
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
}
