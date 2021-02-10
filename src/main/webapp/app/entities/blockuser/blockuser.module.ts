import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { BlockuserComponent } from './blockuser.component';
import { BlockuserDetailComponent } from './blockuser-detail.component';
import { BlockuserUpdateComponent } from './blockuser-update.component';
import { BlockuserDeleteDialogComponent } from './blockuser-delete-dialog.component';
import { blockuserRoute } from './blockuser.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(blockuserRoute)],
  declarations: [BlockuserComponent, BlockuserDetailComponent, BlockuserUpdateComponent, BlockuserDeleteDialogComponent],
  entryComponents: [BlockuserDeleteDialogComponent],
})
export class SpringgoodBlockuserModule {}
