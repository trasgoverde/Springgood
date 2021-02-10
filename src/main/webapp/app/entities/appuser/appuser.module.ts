import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { AppuserComponent } from './appuser.component';
import { AppuserDetailComponent } from './appuser-detail.component';
import { AppuserUpdateComponent } from './appuser-update.component';
import { AppuserDeleteDialogComponent } from './appuser-delete-dialog.component';
import { appuserRoute } from './appuser.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(appuserRoute)],
  declarations: [AppuserComponent, AppuserDetailComponent, AppuserUpdateComponent, AppuserDeleteDialogComponent],
  entryComponents: [AppuserDeleteDialogComponent],
})
export class SpringgoodAppuserModule {}
