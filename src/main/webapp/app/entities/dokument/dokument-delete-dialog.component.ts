import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDokument } from 'app/shared/model/dokument.model';
import { DokumentService } from './dokument.service';

@Component({
  templateUrl: './dokument-delete-dialog.component.html',
})
export class DokumentDeleteDialogComponent {
  dokument?: IDokument;

  constructor(protected dokumentService: DokumentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dokumentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dokumentListModification');
      this.activeModal.close();
    });
  }
}
