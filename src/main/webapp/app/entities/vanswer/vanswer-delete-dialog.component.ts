import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVanswer } from 'app/shared/model/vanswer.model';
import { VanswerService } from './vanswer.service';

@Component({
  templateUrl: './vanswer-delete-dialog.component.html',
})
export class VanswerDeleteDialogComponent {
  vanswer?: IVanswer;

  constructor(protected vanswerService: VanswerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vanswerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vanswerListModification');
      this.activeModal.close();
    });
  }
}
