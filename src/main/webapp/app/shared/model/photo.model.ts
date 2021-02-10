import { Moment } from 'moment';
import { IAlbum } from 'app/shared/model/album.model';
import { ICalbum } from 'app/shared/model/calbum.model';

export interface IPhoto {
  id?: number;
  creationDate?: Moment;
  imageContentType?: string;
  image?: any;
  album?: IAlbum;
  calbum?: ICalbum;
}

export class Photo implements IPhoto {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public imageContentType?: string,
    public image?: any,
    public album?: IAlbum,
    public calbum?: ICalbum
  ) {}
}
