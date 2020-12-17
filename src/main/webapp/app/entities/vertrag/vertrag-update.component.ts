import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVertrag, Vertrag } from 'app/shared/model/vertrag.model';
import { VertragService } from './vertrag.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-vertrag-update',
  templateUrl: './vertrag-update.component.html',
})
export class VertragUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    vsnr: [null, [Validators.required]],
    sparte: [],
    zahlenrhytmus: [],
    antragsdatum: [],
    versicherungsbeginn: [],
    iban: [],
    created: [],
    user: [],
  });

  constructor(
    protected vertragService: VertragService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vertrag }) => {
      if (!vertrag.id) {
        const today = moment().startOf('day');
        vertrag.antragsdatum = today;
        vertrag.versicherungsbeginn = today;
        vertrag.created = today;
      }

      this.updateForm(vertrag);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(vertrag: IVertrag): void {
    this.editForm.patchValue({
      id: vertrag.id,
      vsnr: vertrag.vsnr,
      sparte: vertrag.sparte,
      zahlenrhytmus: vertrag.zahlenrhytmus,
      antragsdatum: vertrag.antragsdatum ? vertrag.antragsdatum.format(DATE_TIME_FORMAT) : null,
      versicherungsbeginn: vertrag.versicherungsbeginn ? vertrag.versicherungsbeginn.format(DATE_TIME_FORMAT) : null,
      iban: vertrag.iban,
      created: vertrag.created ? vertrag.created.format(DATE_TIME_FORMAT) : null,
      user: vertrag.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vertrag = this.createFromForm();
    if (vertrag.id !== undefined) {
      this.subscribeToSaveResponse(this.vertragService.update(vertrag));
    } else {
      this.subscribeToSaveResponse(this.vertragService.create(vertrag));
    }
  }

  private createFromForm(): IVertrag {
    return {
      ...new Vertrag(),
      id: this.editForm.get(['id'])!.value,
      vsnr: this.editForm.get(['vsnr'])!.value,
      sparte: this.editForm.get(['sparte'])!.value,
      zahlenrhytmus: this.editForm.get(['zahlenrhytmus'])!.value,
      antragsdatum: this.editForm.get(['antragsdatum'])!.value
        ? moment(this.editForm.get(['antragsdatum'])!.value, DATE_TIME_FORMAT)
        : undefined,
      versicherungsbeginn: this.editForm.get(['versicherungsbeginn'])!.value
        ? moment(this.editForm.get(['versicherungsbeginn'])!.value, DATE_TIME_FORMAT)
        : undefined,
      iban: this.editForm.get(['iban'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVertrag>>): void {
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
