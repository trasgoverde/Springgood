import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { FollowComponent } from './follow.component';
import { FollowDetailComponent } from './follow-detail.component';
import { FollowUpdateComponent } from './follow-update.component';
import { FollowDeleteDialogComponent } from './follow-delete-dialog.component';
import { followRoute } from './follow.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(followRoute)],
  declarations: [FollowComponent, FollowDetailComponent, FollowUpdateComponent, FollowDeleteDialogComponent],
  entryComponents: [FollowDeleteDialogComponent],
})
export class SpringgoodFollowModule {}
