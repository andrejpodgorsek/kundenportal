import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { DokumentStatus } from 'app/shared/model/enumerations/dokument-status.model';

export interface IDokument {
  id?: number;
  nummer?: string;
  dokument?: DokumentStatus;
  text?: string;
  dataContentType?: string;
  data?: any;
  created?: Moment;
  user?: IUser;
}

export class Dokument implements IDokument {
  constructor(
    public id?: number,
    public nummer?: string,
    public dokument?: DokumentStatus,
    public text?: string,
    public dataContentType?: string,
    public data?: any,
    public created?: Moment,
    public user?: IUser
  ) {}
}
