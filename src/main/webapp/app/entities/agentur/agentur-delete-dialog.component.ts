import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAgentur } from 'app/shared/model/agentur.model';
import { AgenturService } from './agentur.service';

@Component({
  templateUrl: './agentur-delete-dialog.component.html',
})
export class AgenturDeleteDialogComponent {
  agentur?: IAgentur;

  constructor(protected agenturService: AgenturService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agenturService.delete(id).subscribe(() => {
      this.eventManager.broadcast('agenturListModification');
      this.activeModal.close();
    });
  }
}
