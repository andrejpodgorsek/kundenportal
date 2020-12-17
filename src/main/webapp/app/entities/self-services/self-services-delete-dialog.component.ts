import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISelfServices } from 'app/shared/model/self-services.model';
import { SelfServicesService } from './self-services.service';

@Component({
  templateUrl: './self-services-delete-dialog.component.html',
})
export class SelfServicesDeleteDialogComponent {
  selfServices?: ISelfServices;

  constructor(
    protected selfServicesService: SelfServicesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.selfServicesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('selfServicesListModification');
      this.activeModal.close();
    });
  }
}
