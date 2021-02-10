import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CalbumComponent } from './calbum.component';
import { CalbumDetailComponent } from './calbum-detail.component';
import { CalbumUpdateComponent } from './calbum-update.component';
import { CalbumDeleteDialogComponent } from './calbum-delete-dialog.component';
import { calbumRoute } from './calbum.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(calbumRoute)],
  declarations: [CalbumComponent, CalbumDetailComponent, CalbumUpdateComponent, CalbumDeleteDialogComponent],
  entryComponents: [CalbumDeleteDialogComponent],
})
export class SpringgoodCalbumModule {}
