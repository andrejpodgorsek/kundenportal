import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISchade } from 'app/shared/model/schade.model';
import { SchadeService } from './schade.service';

@Component({
  templateUrl: './schade-delete-dialog.component.html',
})
export class SchadeDeleteDialogComponent {
  schade?: ISchade;

  constructor(protected schadeService: SchadeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schadeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('schadeListModification');
      this.activeModal.close();
    });
  }
}
