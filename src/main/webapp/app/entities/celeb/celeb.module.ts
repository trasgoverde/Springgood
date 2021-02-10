import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { CelebComponent } from './celeb.component';
import { CelebDetailComponent } from './celeb-detail.component';
import { CelebUpdateComponent } from './celeb-update.component';
import { CelebDeleteDialogComponent } from './celeb-delete-dialog.component';
import { celebRoute } from './celeb.route';

@NgModule({
  imports: [SpringgoodSharedModule, RouterModule.forChild(celebRoute)],
  declarations: [CelebComponent, CelebDetailComponent, CelebUpdateComponent, CelebDeleteDialogComponent],
  entryComponents: [CelebDeleteDialogComponent],
})
export class SpringgoodCelebModule {}
