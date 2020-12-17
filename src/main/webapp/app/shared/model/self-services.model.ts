import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ServicesTyp } from 'app/shared/model/enumerations/services-typ.model';
import { ServicesStatus } from 'app/shared/model/enumerations/services-status.model';

export interface ISelfServices {
  id?: number;
  serviceTyp?: ServicesTyp;
  status?: ServicesStatus;
  text?: string;
  dateiContentType?: string;
  datei?: any;
  created?: Moment;
  user?: IUser;
}

export class SelfServices implements ISelfServices {
  constructor(
    public id?: number,
    public serviceTyp?: ServicesTyp,
    public status?: ServicesStatus,
    public text?: string,
    public dateiContentType?: string,
    public datei?: any,
    public created?: Moment,
    public user?: IUser
  ) {}
}
