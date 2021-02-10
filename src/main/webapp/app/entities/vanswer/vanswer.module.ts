import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { VanswerComponent } from './vanswer.component';
import { VanswerDetailComponent } from './vanswer-detail.component';
import { VanswerUpdateComponent } from './vanswer-update.component';
import { VanswerDeleteDialogComponent } from './vanswer-delete-dialog.component';
import { vanswerRoute } from './vanswer.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(vanswerRoute)],
  declarations: [VanswerComponent, VanswerDetailComponent, VanswerUpdateComponent, VanswerDeleteDialogComponent],
  entryComponents: [VanswerDeleteDialogComponent],
})
export class SpringgoodVanswerModule {}
