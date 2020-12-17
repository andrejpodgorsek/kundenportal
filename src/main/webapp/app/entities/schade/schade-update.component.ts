import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISchade, Schade } from 'app/shared/model/schade.model';
import { SchadeService } from './schade.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-schade-update',
  templateUrl: './schade-update.component.html',
})
export class SchadeUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    vsnr: [null, [Validators.required]],
    text: [],
    anhang: [],
    anhangContentType: [],
    created: [],
    user: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected schadeService: SchadeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schade }) => {
      if (!schade.id) {
        const today = moment().startOf('day');
        schade.created = today;
      }

      this.updateForm(schade);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(schade: ISchade): void {
    this.editForm.patchValue({
      id: schade.id,
      vsnr: schade.vsnr,
      text: schade.text,
      anhang: schade.anhang,
      anhangContentType: schade.anhangContentType,
      created: schade.created ? schade.created.format(DATE_TIME_FORMAT) : null,
      user: schade.user,
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
    const schade = this.createFromForm();
    if (schade.id !== undefined) {
      this.subscribeToSaveResponse(this.schadeService.update(schade));
    } else {
      this.subscribeToSaveResponse(this.schadeService.create(schade));
    }
  }

  private createFromForm(): ISchade {
    return {
      ...new Schade(),
      id: this.editForm.get(['id'])!.value,
      vsnr: this.editForm.get(['vsnr'])!.value,
      text: this.editForm.get(['text'])!.value,
      anhangContentType: this.editForm.get(['anhangContentType'])!.value,
      anhang: this.editForm.get(['anhang'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchade>>): void {
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
