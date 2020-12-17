import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IKunde } from 'app/shared/model/kunde.model';
import { KundeService } from './kunde.service';

@Component({
  templateUrl: './kunde-delete-dialog.component.html',
})
export class KundeDeleteDialogComponent {
  kunde?: IKunde;

  constructor(protected kundeService: KundeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.kundeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('kundeListModification');
      this.activeModal.close();
    });
  }
}
