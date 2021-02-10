import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalbum } from 'app/shared/model/calbum.model';
import { CalbumService } from './calbum.service';

@Component({
  templateUrl: './calbum-delete-dialog.component.html',
})
export class CalbumDeleteDialogComponent {
  calbum?: ICalbum;

  constructor(protected calbumService: CalbumService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calbumService.delete(id).subscribe(() => {
      this.eventManager.broadcast('calbumListModification');
      this.activeModal.close();
    });
  }
}
