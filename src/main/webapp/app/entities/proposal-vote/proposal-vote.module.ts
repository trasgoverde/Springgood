import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { ProposalVoteComponent } from './proposal-vote.component';
import { ProposalVoteDetailComponent } from './proposal-vote-detail.component';
import { ProposalVoteUpdateComponent } from './proposal-vote-update.component';
import { ProposalVoteDeleteDialogComponent } from './proposal-vote-delete-dialog.component';
import { proposalVoteRoute } from './proposal-vote.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(proposalVoteRoute)],
  declarations: [ProposalVoteComponent, ProposalVoteDetailComponent, ProposalVoteUpdateComponent, ProposalVoteDeleteDialogComponent],
  entryComponents: [ProposalVoteDeleteDialogComponent],
})
export class SpringgoodProposalVoteModule {}
