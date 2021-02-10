import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CactivityComponent } from './cactivity.component';
import { CactivityDetailComponent } from './cactivity-detail.component';
import { CactivityUpdateComponent } from './cactivity-update.component';
import { CactivityDeleteDialogComponent } from './cactivity-delete-dialog.component';
import { cactivityRoute } from './cactivity.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(cactivityRoute)],
  declarations: [CactivityComponent, CactivityDetailComponent, CactivityUpdateComponent, CactivityDeleteDialogComponent],
  entryComponents: [CactivityDeleteDialogComponent],
})
export class SpringgoodCactivityModule {}
