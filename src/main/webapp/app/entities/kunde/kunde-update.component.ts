import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IKunde, Kunde } from 'app/shared/model/kunde.model';
import { KundeService } from './kunde.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-kunde-update',
  templateUrl: './kunde-update.component.html',
})
export class KundeUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    anrede: [null, [Validators.required]],
    name: [null, [Validators.required]],
    vorname: [null, [Validators.required]],
    email: [null, [Validators.required]],
    strasse: [],
    plzort: [],
    telefonnr: [],
    iban: [],
    created: [],
    user: [],
  });

  constructor(
    protected kundeService: KundeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kunde }) => {
      if (!kunde.id) {
        const today = moment().startOf('day');
        kunde.created = today;
      }

      this.updateForm(kunde);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(kunde: IKunde): void {
    this.editForm.patchValue({
      id: kunde.id,
      anrede: kunde.anrede,
      name: kunde.name,
      vorname: kunde.vorname,
      email: kunde.email,
      strasse: kunde.strasse,
      plzort: kunde.plzort,
      telefonnr: kunde.telefonnr,
      iban: kunde.iban,
      created: kunde.created ? kunde.created.format(DATE_TIME_FORMAT) : null,
      user: kunde.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const kunde = this.createFromForm();
    if (kunde.id !== undefined) {
      this.subscribeToSaveResponse(this.kundeService.update(kunde));
    } else {
      this.subscribeToSaveResponse(this.kundeService.create(kunde));
    }
  }

  private createFromForm(): IKunde {
    return {
      ...new Kunde(),
      id: this.editForm.get(['id'])!.value,
      anrede: this.editForm.get(['anrede'])!.value,
      name: this.editForm.get(['name'])!.value,
      vorname: this.editForm.get(['vorname'])!.value,
      email: this.editForm.get(['email'])!.value,
      strasse: this.editForm.get(['strasse'])!.value,
      plzort: this.editForm.get(['plzort'])!.value,
      telefonnr: this.editForm.get(['telefonnr'])!.value,
      iban: this.editForm.get(['iban'])!.value,
      created: this.editForm.get(['created'])!.value ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKunde>>): void {
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
