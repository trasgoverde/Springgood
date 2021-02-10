import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { ProposalComponent } from './proposal.component';
import { ProposalDetailComponent } from './proposal-detail.component';
import { ProposalUpdateComponent } from './proposal-update.component';
import { ProposalDeleteDialogComponent } from './proposal-delete-dialog.component';
import { proposalRoute } from './proposal.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(proposalRoute)],
  declarations: [ProposalComponent, ProposalDetailComponent, ProposalUpdateComponent, ProposalDeleteDialogComponent],
  entryComponents: [ProposalDeleteDialogComponent],
})
export class SpringgoodProposalModule {}
