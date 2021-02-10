import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CommunityComponent } from './community.component';
import { CommunityDetailComponent } from './community-detail.component';
import { CommunityUpdateComponent } from './community-update.component';
import { CommunityDeleteDialogComponent } from './community-delete-dialog.component';
import { communityRoute } from './community.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(communityRoute)],
  declarations: [CommunityComponent, CommunityDetailComponent, CommunityUpdateComponent, CommunityDeleteDialogComponent],
  entryComponents: [CommunityDeleteDialogComponent],
})
export class SpringgoodCommunityModule {}
