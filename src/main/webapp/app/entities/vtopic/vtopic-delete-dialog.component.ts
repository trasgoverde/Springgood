import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVtopic } from 'app/shared/model/vtopic.model';
import { VtopicService } from './vtopic.service';

@Component({
  templateUrl: './vtopic-delete-dialog.component.html',
})
export class VtopicDeleteDialogComponent {
  vtopic?: IVtopic;

  constructor(protected vtopicService: VtopicService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vtopicService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vtopicListModification');
      this.activeModal.close();
    });
  }
}
