import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVthumb } from 'app/shared/model/vthumb.model';
import { VthumbService } from './vthumb.service';

@Component({
  templateUrl: './vthumb-delete-dialog.component.html',
})
export class VthumbDeleteDialogComponent {
  vthumb?: IVthumb;

  constructor(protected vthumbService: VthumbService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vthumbService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vthumbListModification');
      this.activeModal.close();
    });
  }
}
