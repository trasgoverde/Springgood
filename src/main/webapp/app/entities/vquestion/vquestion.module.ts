import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { VquestionComponent } from './vquestion.component';
import { VquestionDetailComponent } from './vquestion-detail.component';
import { VquestionUpdateComponent } from './vquestion-update.component';
import { VquestionDeleteDialogComponent } from './vquestion-delete-dialog.component';
import { vquestionRoute } from './vquestion.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(vquestionRoute)],
  declarations: [VquestionComponent, VquestionDetailComponent, VquestionUpdateComponent, VquestionDeleteDialogComponent],
  entryComponents: [VquestionDeleteDialogComponent],
})
export class SpringgoodVquestionModule {}
