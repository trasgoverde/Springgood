import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IVthumb } from 'app/shared/model/vthumb.model';
import { IVquestion } from 'app/shared/model/vquestion.model';

export interface IVanswer {
  id?: number;
  creationDate?: Moment;
  urlVanswer?: string;
  accepted?: boolean;
  appusers?: IAppuser[];
  vthumbs?: IVthumb[];
  vquestion?: IVquestion;
}

export class Vanswer implements IVanswer {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public urlVanswer?: string,
    public accepted?: boolean,
    public appusers?: IAppuser[],
    public vthumbs?: IVthumb[],
    public vquestion?: IVquestion
  ) {
    this.accepted = this.accepted || false;
  }
}
