import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { VthumbComponent } from './vthumb.component';
import { VthumbDetailComponent } from './vthumb-detail.component';
import { VthumbUpdateComponent } from './vthumb-update.component';
import { VthumbDeleteDialogComponent } from './vthumb-delete-dialog.component';
import { vthumbRoute } from './vthumb.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(vthumbRoute)],
  declarations: [VthumbComponent, VthumbDetailComponent, VthumbUpdateComponent, VthumbDeleteDialogComponent],
  entryComponents: [VthumbDeleteDialogComponent],
})
export class SpringgoodVthumbModule {}
