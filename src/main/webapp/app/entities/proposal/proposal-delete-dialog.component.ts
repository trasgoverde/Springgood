import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProposal } from 'app/shared/model/proposal.model';
import { ProposalService } from './proposal.service';

@Component({
  templateUrl: './proposal-delete-dialog.component.html',
})
export class ProposalDeleteDialogComponent {
  proposal?: IProposal;

  constructor(protected proposalService: ProposalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proposalService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proposalListModification');
      this.activeModal.close();
    });
  }
}
