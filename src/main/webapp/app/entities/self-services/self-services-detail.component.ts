import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISelfServices } from 'app/shared/model/self-services.model';

@Component({
  selector: 'jhi-self-services-detail',
  templateUrl: './self-services-detail.component.html',
})
export class SelfServicesDetailComponent implements OnInit {
  selfServices: ISelfServices | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selfServices }) => (this.selfServices = selfServices));
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
