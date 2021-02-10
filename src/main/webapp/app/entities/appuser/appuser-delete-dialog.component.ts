import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAppuser } from 'app/shared/model/appuser.model';
import { AppuserService } from './appuser.service';

@Component({
  templateUrl: './appuser-delete-dialog.component.html',
})
export class AppuserDeleteDialogComponent {
  appuser?: IAppuser;

  constructor(protected appuserService: AppuserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appuserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('appuserListModification');
      this.activeModal.close();
    });
  }
}
