import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommunity } from 'app/shared/model/community.model';
import { CommunityService } from './community.service';

@Component({
  templateUrl: './community-delete-dialog.component.html',
})
export class CommunityDeleteDialogComponent {
  community?: ICommunity;

  constructor(protected communityService: CommunityService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('communityListModification');
      this.activeModal.close();
    });
  }
}
