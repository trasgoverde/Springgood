import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { BmessageComponent } from './bmessage.component';
import { BmessageDetailComponent } from './bmessage-detail.component';
import { BmessageUpdateComponent } from './bmessage-update.component';
import { BmessageDeleteDialogComponent } from './bmessage-delete-dialog.component';
import { bmessageRoute } from './bmessage.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(bmessageRoute)],
  declarations: [BmessageComponent, BmessageDetailComponent, BmessageUpdateComponent, BmessageDeleteDialogComponent],
  entryComponents: [BmessageDeleteDialogComponent],
})
export class SpringgoodBmessageModule {}
