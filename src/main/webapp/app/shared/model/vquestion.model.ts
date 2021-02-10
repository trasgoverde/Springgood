import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IVanswer } from 'app/shared/model/vanswer.model';
import { IVthumb } from 'app/shared/model/vthumb.model';
import { IVtopic } from 'app/shared/model/vtopic.model';

export interface IVquestion {
  id?: number;
  creationDate?: Moment;
  vquestion?: string;
  vquestionDescription?: string;
  appusers?: IAppuser[];
  vanswers?: IVanswer[];
  vthumbs?: IVthumb[];
  vtopic?: IVtopic;
}

export class Vquestion implements IVquestion {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public vquestion?: string,
    public vquestionDescription?: string,
    public appusers?: IAppuser[],
    public vanswers?: IVanswer[],
    public vthumbs?: IVthumb[],
    public vtopic?: IVtopic
  ) {}
}
