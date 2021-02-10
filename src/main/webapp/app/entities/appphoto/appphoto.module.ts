import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { AppphotoComponent } from './appphoto.component';
import { AppphotoDetailComponent } from './appphoto-detail.component';
import { AppphotoUpdateComponent } from './appphoto-update.component';
import { AppphotoDeleteDialogComponent } from './appphoto-delete-dialog.component';
import { appphotoRoute } from './appphoto.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(appphotoRoute)],
  declarations: [AppphotoComponent, AppphotoDetailComponent, AppphotoUpdateComponent, AppphotoDeleteDialogComponent],
  entryComponents: [AppphotoDeleteDialogComponent],
})
export class SpringgoodAppphotoModule {}
