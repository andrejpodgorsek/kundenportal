import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVertrag } from 'app/shared/model/vertrag.model';
import { VertragService } from './vertrag.service';

@Component({
  templateUrl: './vertrag-delete-dialog.component.html',
})
export class VertragDeleteDialogComponent {
  vertrag?: IVertrag;

  constructor(protected vertragService: VertragService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vertragService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vertragListModification');
      this.activeModal.close();
    });
  }
}
