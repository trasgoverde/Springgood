import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProposalVote, ProposalVote } from 'app/shared/model/proposal-vote.model';
import { ProposalVoteService } from './proposal-vote.service';
import { IProposal } from 'app/shared/model/proposal.model';
import { ProposalService } from 'app/entities/proposal/proposal.service';

@Component({
  selector: 'jhi-proposal-vote-update',
  templateUrl: './proposal-vote-update.component.html',
})
export class ProposalVoteUpdateComponent implements OnInit {
  isSaving = false;
  proposals: IProposal[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    votePoints: [null, [Validators.required]],
    proposal: [],
  });

  constructor(
    protected proposalVoteService: ProposalVoteService,
    protected proposalService: ProposalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposalVote }) => {
      if (!proposalVote.id) {
        const today = moment().startOf('day');
        proposalVote.creationDate = today;
      }

      this.updateForm(proposalVote);

      this.proposalService.query().subscribe((res: HttpResponse<IProposal[]>) => (this.proposals = res.body || []));
    });
  }

  updateForm(proposalVote: IProposalVote): void {
    this.editForm.patchValue({
      id: proposalVote.id,
      creationDate: proposalVote.creationDate ? proposalVote.creationDate.format(DATE_TIME_FORMAT) : null,
      votePoints: proposalVote.votePoints,
      proposal: proposalVote.proposal,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proposalVote = this.createFromForm();
    if (proposalVote.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalVoteService.update(proposalVote));
    } else {
      this.subscribeToSaveResponse(this.proposalVoteService.create(proposalVote));
    }
  }

  private createFromForm(): IProposalVote {
    return {
      ...new ProposalVote(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? moment(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      votePoints: this.editForm.get(['votePoints'])!.value,
      proposal: this.editForm.get(['proposal'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposalVote>>): void {
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

  trackById(index: number, item: IProposal): any {
    return item.id;
  }
}
