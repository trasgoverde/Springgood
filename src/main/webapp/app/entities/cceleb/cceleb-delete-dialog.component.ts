import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICceleb } from 'app/shared/model/cceleb.model';
import { CcelebService } from './cceleb.service';

@Component({
  templateUrl: './cceleb-delete-dialog.component.html',
})
export class CcelebDeleteDialogComponent {
  cceleb?: ICceleb;

  constructor(protected ccelebService: CcelebService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ccelebService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ccelebListModification');
      this.activeModal.close();
    });
  }
}
