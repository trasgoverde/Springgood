import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICactivity } from 'app/shared/model/cactivity.model';
import { CactivityService } from './cactivity.service';

@Component({
  templateUrl: './cactivity-delete-dialog.component.html',
})
export class CactivityDeleteDialogComponent {
  cactivity?: ICactivity;

  constructor(protected cactivityService: CactivityService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cactivityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cactivityListModification');
      this.activeModal.close();
    });
  }
}
