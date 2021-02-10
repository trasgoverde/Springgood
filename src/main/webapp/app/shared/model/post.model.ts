import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IComment } from 'app/shared/model/comment.model';
import { IProposal } from 'app/shared/model/proposal.model';
import { IBlog } from 'app/shared/model/blog.model';
import { ITag } from 'app/shared/model/tag.model';
import { ITopic } from 'app/shared/model/topic.model';

export interface IPost {
  id?: number;
  creationDate?: Moment;
  publicationDate?: Moment;
  headline?: string;
  leadtext?: string;
  bodytext?: string;
  quote?: string;
  conclusion?: string;
  linkText?: string;
  linkURL?: string;
  imageContentType?: string;
  image?: any;
  appusers?: IAppuser[];
  comments?: IComment[];
  proposals?: IProposal[];
  blog?: IBlog;
  tags?: ITag[];
  topics?: ITopic[];
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public publicationDate?: Moment,
    public headline?: string,
    public leadtext?: string,
    public bodytext?: string,
    public quote?: string,
    public conclusion?: string,
    public linkText?: string,
    public linkURL?: string,
    public imageContentType?: string,
    public image?: any,
    public appusers?: IAppuser[],
    public comments?: IComment[],
    public proposals?: IProposal[],
    public blog?: IBlog,
    public tags?: ITag[],
    public topics?: ITopic[]
  ) {}
}
