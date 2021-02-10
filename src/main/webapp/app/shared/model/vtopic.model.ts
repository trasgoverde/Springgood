import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IVquestion } from 'app/shared/model/vquestion.model';

export interface IVtopic {
  id?: number;
  creationDate?: Moment;
  vtopicTitle?: string;
  vtopicDescription?: string;
  appusers?: IAppuser[];
  vquestions?: IVquestion[];
}

export class Vtopic implements IVtopic {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public vtopicTitle?: string,
    public vtopicDescription?: string,
    public appusers?: IAppuser[],
    public vquestions?: IVquestion[]
  ) {}
}
