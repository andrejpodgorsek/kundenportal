import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { NachrichtStatus } from 'app/shared/model/enumerations/nachricht-status.model';

export interface INachricht {
  id?: number;
  an?: string;
  betreff?: string;
  text?: string;
  anhangContentType?: string;
  anhang?: any;
  status?: NachrichtStatus;
  created?: Moment;
  user?: IUser;
}

export class Nachricht implements INachricht {
  constructor(
    public id?: number,
    public an?: string,
    public betreff?: string,
    public text?: string,
    public anhangContentType?: string,
    public anhang?: any,
    public status?: NachrichtStatus,
    public created?: Moment,
    public user?: IUser
  ) {}
}
