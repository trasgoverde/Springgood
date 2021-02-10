import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CmessageComponent } from './cmessage.component';
import { CmessageDetailComponent } from './cmessage-detail.component';
import { CmessageUpdateComponent } from './cmessage-update.component';
import { CmessageDeleteDialogComponent } from './cmessage-delete-dialog.component';
import { cmessageRoute } from './cmessage.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(cmessageRoute)],
  declarations: [CmessageComponent, CmessageDetailComponent, CmessageUpdateComponent, CmessageDeleteDialogComponent],
  entryComponents: [CmessageDeleteDialogComponent],
})
export class SpringgoodCmessageModule {}
