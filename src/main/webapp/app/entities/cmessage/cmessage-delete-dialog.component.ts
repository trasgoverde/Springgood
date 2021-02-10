import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICmessage } from 'app/shared/model/cmessage.model';
import { CmessageService } from './cmessage.service';

@Component({
  templateUrl: './cmessage-delete-dialog.component.html',
})
export class CmessageDeleteDialogComponent {
  cmessage?: ICmessage;

  constructor(protected cmessageService: CmessageService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cmessageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cmessageListModification');
      this.activeModal.close();
    });
  }
}
