import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAgentur, Agentur } from 'app/shared/model/agentur.model';
import { AgenturService } from './agentur.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-agentur-update',
  templateUrl: './agentur-update.component.html',
})
export class AgenturUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    agenturnummer: [null, [Validators.required]],
    name: [],
    adresse: [],
    email: [],
    telefonnr: [],
    created: [],
    user: [],
  });

  constructor(
    protected agenturService: AgenturService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentur }) => {
      if (!agentur.id) {
        const today = moment().startOf('day');
        agentur.created = today;
      }

      this.updateForm(agentur);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(agentur: IAgentur): void {
    this.editForm.patchValue({
      id: agentur.id,
      agenturnummer: agentur.agenturnummer,
      name: agentur.name,
      adresse: agentur.adresse,
      email: agentur.email,
      telefonnr: agentur.telefonnr,
      created: agentur.created ? agentur.created.format(DATE_TIME_FORMAT) : null,
      user: agentur.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agentur = this.createFromForm();
    if (agentur.id !== undefined) {
      this.subscribeToSaveResponse(this.agenturService.update(agentur));
    } else {
      this.subscribeToSaveResponse(this.agenturService.create(agentur));
    }
  }

  private createFromForm(): IAgentur {
    return {
      ...new Agentur(),
      id: this.editForm.get(['id'])!.value,
      agenturnummer: this.editForm.get(['agenturnummer'])!.value,
      name: this.editForm.get(['name'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      email: this.editForm.get(['email'])!.value,
      telefonnr: this.editForm.get(['telefonnr'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgentur>>): void {
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
