import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { INachricht } from 'app/shared/model/nachricht.model';

@Component({
  selector: 'jhi-nachricht-detail',
  templateUrl: './nachricht-detail.component.html',
})
export class NachrichtDetailComponent implements OnInit {
  nachricht: INachricht | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nachricht }) => (this.nachricht = nachricht));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
