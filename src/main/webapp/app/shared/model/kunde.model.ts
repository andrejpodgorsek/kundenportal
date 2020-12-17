import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { Anrede } from 'app/shared/model/enumerations/anrede.model';

export interface IKunde {
  id?: number;
  anrede?: Anrede;
  name?: string;
  vorname?: string;
  email?: string;
  strasse?: string;
  plzort?: string;
  telefonnr?: string;
  iban?: string;
  created?: Moment;
  user?: IUser;
}

export class Kunde implements IKunde {
  constructor(
    public id?: number,
    public anrede?: Anrede,
    public name?: string,
    public vorname?: string,
    public email?: string,
    public strasse?: string,
    public plzort?: string,
    public telefonnr?: string,
    public iban?: string,
    public created?: Moment,
    public user?: IUser
  ) {}
}
