import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { Sparte } from 'app/shared/model/enumerations/sparte.model';
import { Rhytmus } from 'app/shared/model/enumerations/rhytmus.model';

export interface IVertrag {
  id?: number;
  vsnr?: string;
  sparte?: Sparte;
  zahlenrhytmus?: Rhytmus;
  antragsdatum?: Moment;
  versicherungsbeginn?: Moment;
  iban?: string;
  created?: Moment;
  user?: IUser;
}

export class Vertrag implements IVertrag {
  constructor(
    public id?: number,
    public vsnr?: string,
    public sparte?: Sparte,
    public zahlenrhytmus?: Rhytmus,
    public antragsdatum?: Moment,
    public versicherungsbeginn?: Moment,
    public iban?: string,
    public created?: Moment,
    public user?: IUser
  ) {}
}
