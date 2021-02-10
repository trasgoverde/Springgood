import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFollow } from 'app/shared/model/follow.model';
import { FollowService } from './follow.service';

@Component({
  templateUrl: './follow-delete-dialog.component.html',
})
export class FollowDeleteDialogComponent {
  follow?: IFollow;

  constructor(protected followService: FollowService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.followService.delete(id).subscribe(() => {
      this.eventManager.broadcast('followListModification');
      this.activeModal.close();
    });
  }
}
