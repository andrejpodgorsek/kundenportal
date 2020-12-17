import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INachricht } from 'app/shared/model/nachricht.model';
import { NachrichtService } from './nachricht.service';

@Component({
  templateUrl: './nachricht-delete-dialog.component.html',
})
export class NachrichtDeleteDialogComponent {
  nachricht?: INachricht;

  constructor(protected nachrichtService: NachrichtService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nachrichtService.delete(id).subscribe(() => {
      this.eventManager.broadcast('nachrichtListModification');
      this.activeModal.close();
    });
  }
}
