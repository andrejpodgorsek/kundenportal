import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface ISchade {
  id?: number;
  vsnr?: string;
  text?: string;
  anhangContentType?: string;
  anhang?: any;
  created?: Moment;
  user?: IUser;
}

export class Schade implements ISchade {
  constructor(
    public id?: number,
    public vsnr?: string,
    public text?: string,
    public anhangContentType?: string,
    public anhang?: any,
    public created?: Moment,
    public user?: IUser
  ) {}
}
