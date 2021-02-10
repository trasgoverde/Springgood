import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CcelebComponent } from './cceleb.component';
import { CcelebDetailComponent } from './cceleb-detail.component';
import { CcelebUpdateComponent } from './cceleb-update.component';
import { CcelebDeleteDialogComponent } from './cceleb-delete-dialog.component';
import { ccelebRoute } from './cceleb.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(ccelebRoute)],
  declarations: [CcelebComponent, CcelebDetailComponent, CcelebUpdateComponent, CcelebDeleteDialogComponent],
  entryComponents: [CcelebDeleteDialogComponent],
})
export class SpringgoodCcelebModule {}
