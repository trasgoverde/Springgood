import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from './vquestion.service';

@Component({
  templateUrl: './vquestion-delete-dialog.component.html',
})
export class VquestionDeleteDialogComponent {
  vquestion?: IVquestion;

  constructor(protected vquestionService: VquestionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vquestionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vquestionListModification');
      this.activeModal.close();
    });
  }
}
