import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { INachricht, Nachricht } from 'app/shared/model/nachricht.model';
import { NachrichtService } from './nachricht.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-nachricht-update',
  templateUrl: './nachricht-update.component.html',
})
export class NachrichtUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    an: [null, [Validators.required]],
    betreff: [null, [Validators.required]],
    text: [null, [Validators.required]],
    anhang: [],
    anhangContentType: [],
    status: [],
    created: [],
    user: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected nachrichtService: NachrichtService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nachricht }) => {
      if (!nachricht.id) {
        const today = moment().startOf('day');
        nachricht.created = today;
      }

      this.updateForm(nachricht);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(nachricht: INachricht): void {
    this.editForm.patchValue({
      id: nachricht.id,
      an: nachricht.an,
      betreff: nachricht.betreff,
      text: nachricht.text,
      anhang: nachricht.anhang,
      anhangContentType: nachricht.anhangContentType,
      status: nachricht.status,
      created: nachricht.created ? nachricht.created.format(DATE_TIME_FORMAT) : null,
      user: nachricht.user,
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
    const nachricht = this.createFromForm();
    if (nachricht.id !== undefined) {
      this.subscribeToSaveResponse(this.nachrichtService.update(nachricht));
    } else {
      this.subscribeToSaveResponse(this.nachrichtService.create(nachricht));
    }
  }

  private createFromForm(): INachricht {
    return {
      ...new Nachricht(),
      id: this.editForm.get(['id'])!.value,
      an: this.editForm.get(['an'])!.value,
      betreff: this.editForm.get(['betreff'])!.value,
      text: this.editForm.get(['text'])!.value,
      anhangContentType: this.editForm.get(['anhangContentType'])!.value,
      anhang: this.editForm.get(['anhang'])!.value,
      status: this.editForm.get(['status'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INachricht>>): void {
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
