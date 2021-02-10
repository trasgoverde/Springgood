import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IVquestion } from 'app/shared/model/vquestion.model';
import { IVanswer } from 'app/shared/model/vanswer.model';

export interface IVthumb {
  id?: number;
  creationDate?: Moment;
  vthumbUp?: boolean;
  vthumbDown?: boolean;
  appusers?: IAppuser[];
  vquestion?: IVquestion;
  vanswer?: IVanswer;
}

export class Vthumb implements IVthumb {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public vthumbUp?: boolean,
    public vthumbDown?: boolean,
    public appusers?: IAppuser[],
    public vquestion?: IVquestion,
    public vanswer?: IVanswer
  ) {
    this.vthumbUp = this.vthumbUp || false;
    this.vthumbDown = this.vthumbDown || false;
  }
}
