import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CinterestComponent } from './cinterest.component';
import { CinterestDetailComponent } from './cinterest-detail.component';
import { CinterestUpdateComponent } from './cinterest-update.component';
import { CinterestDeleteDialogComponent } from './cinterest-delete-dialog.component';
import { cinterestRoute } from './cinterest.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(cinterestRoute)],
  declarations: [CinterestComponent, CinterestDetailComponent, CinterestUpdateComponent, CinterestDeleteDialogComponent],
  entryComponents: [CinterestDeleteDialogComponent],
})
export class SpringgoodCinterestModule {}
