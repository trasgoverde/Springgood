import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICeleb } from 'app/shared/model/celeb.model';
import { CelebService } from './celeb.service';

@Component({
  templateUrl: './celeb-delete-dialog.component.html',
})
export class CelebDeleteDialogComponent {
  celeb?: ICeleb;

  constructor(protected celebService: CelebService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.celebService.delete(id).subscribe(() => {
      this.eventManager.broadcast('celebListModification');
      this.activeModal.close();
    });
  }
}
