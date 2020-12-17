import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDokument, Dokument } from 'app/shared/model/dokument.model';
import { DokumentService } from './dokument.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-dokument-update',
  templateUrl: './dokument-update.component.html',
})
export class DokumentUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nummer: [null, [Validators.required]],
    dokument: [],
    text: [],
    data: [],
    dataContentType: [],
    created: [],
    user: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected dokumentService: DokumentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dokument }) => {
      if (!dokument.id) {
        const today = moment().startOf('day');
        dokument.created = today;
      }

      this.updateForm(dokument);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(dokument: IDokument): void {
    this.editForm.patchValue({
      id: dokument.id,
      nummer: dokument.nummer,
      dokument: dokument.dokument,
      text: dokument.text,
      data: dokument.data,
      dataContentType: dokument.dataContentType,
      created: dokument.created ? dokument.created.format(DATE_TIME_FORMAT) : null,
      user: dokument.user,
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
    const dokument = this.createFromForm();
    if (dokument.id !== undefined) {
      this.subscribeToSaveResponse(this.dokumentService.update(dokument));
    } else {
      this.subscribeToSaveResponse(this.dokumentService.create(dokument));
    }
  }

  private createFromForm(): IDokument {
    return {
      ...new Dokument(),
      id: this.editForm.get(['id'])!.value,
      nummer: this.editForm.get(['nummer'])!.value,
      dokument: this.editForm.get(['dokument'])!.value,
      text: this.editForm.get(['text'])!.value,
      dataContentType: this.editForm.get(['dataContentType'])!.value,
      data: this.editForm.get(['data'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDokument>>): void {
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
