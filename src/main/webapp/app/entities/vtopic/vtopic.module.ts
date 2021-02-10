import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { VtopicComponent } from './vtopic.component';
import { VtopicDetailComponent } from './vtopic-detail.component';
import { VtopicUpdateComponent } from './vtopic-update.component';
import { VtopicDeleteDialogComponent } from './vtopic-delete-dialog.component';
import { vtopicRoute } from './vtopic.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(vtopicRoute)],
  declarations: [VtopicComponent, VtopicDetailComponent, VtopicUpdateComponent, VtopicDeleteDialogComponent],
  entryComponents: [VtopicDeleteDialogComponent],
})
export class SpringgoodVtopicModule {}
