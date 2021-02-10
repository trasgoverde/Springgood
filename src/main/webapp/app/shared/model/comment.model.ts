import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IPost } from 'app/shared/model/post.model';

export interface IComment {
  id?: number;
  creationDate?: Moment;
  commentText?: string;
  isOffensive?: boolean;
  appusers?: IAppuser[];
  post?: IPost;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public commentText?: string,
    public isOffensive?: boolean,
    public appusers?: IAppuser[],
    public post?: IPost
  ) {
    this.isOffensive = this.isOffensive || false;
  }
}
