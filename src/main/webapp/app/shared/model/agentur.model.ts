import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IAgentur {
  id?: number;
  agenturnummer?: string;
  name?: string;
  adresse?: string;
  email?: string;
  telefonnr?: string;
  created?: Moment;
  user?: IUser;
}

export class Agentur implements IAgentur {
  constructor(
    public id?: number,
    public agenturnummer?: string,
    public name?: string,
    public adresse?: string,
    public email?: string,
    public telefonnr?: string,
    public created?: Moment,
    public user?: IUser
  ) {}
}
