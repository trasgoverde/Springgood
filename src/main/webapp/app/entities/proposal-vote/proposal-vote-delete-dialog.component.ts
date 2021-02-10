import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProposalVote } from 'app/shared/model/proposal-vote.model';
import { ProposalVoteService } from './proposal-vote.service';

@Component({
  templateUrl: './proposal-vote-delete-dialog.component.html',
})
export class ProposalVoteDeleteDialogComponent {
  proposalVote?: IProposalVote;

  constructor(
    protected proposalVoteService: ProposalVoteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proposalVoteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proposalVoteListModification');
      this.activeModal.close();
    });
  }
}
