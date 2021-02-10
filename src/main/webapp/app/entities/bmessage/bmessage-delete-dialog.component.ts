import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBmessage } from 'app/shared/model/bmessage.model';
import { BmessageService } from './bmessage.service';

@Component({
  templateUrl: './bmessage-delete-dialog.component.html',
})
export class BmessageDeleteDialogComponent {
  bmessage?: IBmessage;

  constructor(protected bmessageService: BmessageService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bmessageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bmessageListModification');
      this.activeModal.close();
    });
  }
}
