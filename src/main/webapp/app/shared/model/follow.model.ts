import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { ICommunity } from 'app/shared/model/community.model';

export interface IFollow {
  id?: number;
  creationDate?: Moment;
  followeds?: IAppuser[];
  followings?: IAppuser[];
  cfollowed?: ICommunity;
  cfollowing?: ICommunity;
}

export class Follow implements IFollow {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public followeds?: IAppuser[],
    public followings?: IAppuser[],
    public cfollowed?: ICommunity,
    public cfollowing?: ICommunity
  ) {}
}
