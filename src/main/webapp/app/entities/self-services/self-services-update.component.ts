import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISelfServices, SelfServices } from 'app/shared/model/self-services.model';
import { SelfServicesService } from './self-services.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-self-services-update',
  templateUrl: './self-services-update.component.html',
})
export class SelfServicesUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    serviceTyp: [null, [Validators.required]],
    status: [null, [Validators.required]],
    text: [],
    datei: [],
    dateiContentType: [],
    created: [],
    user: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected selfServicesService: SelfServicesService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selfServices }) => {
      if (!selfServices.id) {
        const today = moment().startOf('day');
        selfServices.created = today;
      }

      this.updateForm(selfServices);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(selfServices: ISelfServices): void {
    this.editForm.patchValue({
      id: selfServices.id,
      serviceTyp: selfServices.serviceTyp,
      status: selfServices.status,
      text: selfServices.text,
      datei: selfServices.datei,
      dateiContentType: selfServices.dateiContentType,
      created: selfServices.created ? selfServices.created.format(DATE_TIME_FORMAT) : null,
      user: selfServices.user,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('kundenportalApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const selfServices = this.createFromForm();
    if (selfServices.id !== undefined) {
      this.subscribeToSaveResponse(this.selfServicesService.update(selfServices));
    } else {
      this.subscribeToSaveResponse(this.selfServicesService.create(selfServices));
    }
  }

  private createFromForm(): ISelfServices {
    return {
      ...new SelfServices(),
      id: this.editForm.get(['id'])!.value,
      serviceTyp: this.editForm.get(['serviceTyp'])!.value,
      status: this.editForm.get(['status'])!.value,
      text: this.editForm.get(['text'])!.value,
      dateiContentType: this.editForm.get(['dateiContentType'])!.value,
      datei: this.editForm.get(['datei'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISelfServices>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
