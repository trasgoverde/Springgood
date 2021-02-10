import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAppphoto } from 'app/shared/model/appphoto.model';
import { AppphotoService } from './appphoto.service';

@Component({
  templateUrl: './appphoto-delete-dialog.component.html',
})
export class AppphotoDeleteDialogComponent {
  appphoto?: IAppphoto;

  constructor(protected appphotoService: AppphotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appphotoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('appphotoListModification');
      this.activeModal.close();
    });
  }
}
