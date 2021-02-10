import { Moment } from 'moment';
import { ICommunity } from 'app/shared/model/community.model';
import { IPhoto } from 'app/shared/model/photo.model';

export interface ICalbum {
  id?: number;
  creationDate?: Moment;
  title?: string;
  communities?: ICommunity[];
  photos?: IPhoto[];
}

export class Calbum implements ICalbum {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public title?: string,
    public communities?: ICommunity[],
    public photos?: IPhoto[]
  ) {}
}
