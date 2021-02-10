import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';

export interface IAppphoto {
  id?: number;
  creationDate?: Moment;
  imageContentType?: string;
  image?: any;
  appuser?: IAppuser;
}

export class Appphoto implements IAppphoto {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public imageContentType?: string,
    public image?: any,
    public appuser?: IAppuser
  ) {}
}
