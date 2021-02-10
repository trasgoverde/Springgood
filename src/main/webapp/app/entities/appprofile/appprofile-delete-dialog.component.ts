import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAppprofile } from 'app/shared/model/appprofile.model';
import { AppprofileService } from './appprofile.service';

@Component({
  templateUrl: './appprofile-delete-dialog.component.html',
})
export class AppprofileDeleteDialogComponent {
  appprofile?: IAppprofile;

  constructor(
    protected appprofileService: AppprofileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appprofileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('appprofileListModification');
      this.activeModal.close();
    });
  }
}
