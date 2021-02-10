import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IPhoto } from 'app/shared/model/photo.model';

export interface IAlbum {
  id?: number;
  creationDate?: Moment;
  title?: string;
  appusers?: IAppuser[];
  photos?: IPhoto[];
}

export class Album implements IAlbum {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public title?: string,
    public appusers?: IAppuser[],
    public photos?: IPhoto[]
  ) {}
}
