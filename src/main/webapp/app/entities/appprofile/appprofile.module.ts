import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { AppprofileComponent } from './appprofile.component';
import { AppprofileDetailComponent } from './appprofile-detail.component';
import { AppprofileUpdateComponent } from './appprofile-update.component';
import { AppprofileDeleteDialogComponent } from './appprofile-delete-dialog.component';
import { appprofileRoute } from './appprofile.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(appprofileRoute)],
  declarations: [AppprofileComponent, AppprofileDetailComponent, AppprofileUpdateComponent, AppprofileDeleteDialogComponent],
  entryComponents: [AppprofileDeleteDialogComponent],
})
export class SpringgoodAppprofileModule {}
