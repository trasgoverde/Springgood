import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICinterest } from 'app/shared/model/cinterest.model';
import { CinterestService } from './cinterest.service';

@Component({
  templateUrl: './cinterest-delete-dialog.component.html',
})
export class CinterestDeleteDialogComponent {
  cinterest?: ICinterest;

  constructor(protected cinterestService: CinterestService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cinterestService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cinterestListModification');
      this.activeModal.close();
    });
  }
}
