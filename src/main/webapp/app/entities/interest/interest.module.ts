import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { InterestComponent } from './interest.component';
import { InterestDetailComponent } from './interest-detail.component';
import { InterestUpdateComponent } from './interest-update.component';
import { InterestDeleteDialogComponent } from './interest-delete-dialog.component';
import { interestRoute } from './interest.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(interestRoute)],
  declarations: [InterestComponent, InterestDetailComponent, InterestUpdateComponent, InterestDeleteDialogComponent],
  entryComponents: [InterestDeleteDialogComponent],
})
export class SpringgoodInterestModule {}
