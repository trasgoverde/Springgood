import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IPost } from 'app/shared/model/post.model';
import { ICommunity } from 'app/shared/model/community.model';

export interface IBlog {
  id?: number;
  creationDate?: Moment;
  title?: string;
  imageContentType?: string;
  image?: any;
  appusers?: IAppuser[];
  posts?: IPost[];
  community?: ICommunity;
}

export class Blog implements IBlog {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public title?: string,
    public imageContentType?: string,
    public image?: any,
    public appusers?: IAppuser[],
    public posts?: IPost[],
    public community?: ICommunity
  ) {}
}
