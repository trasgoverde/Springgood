import { Moment } from 'moment';
import { IAppprofile } from 'app/shared/model/appprofile.model';
import { IAppphoto } from 'app/shared/model/appphoto.model';
import { ICommunity } from 'app/shared/model/community.model';
import { IBlog } from 'app/shared/model/blog.model';
import { INotification } from 'app/shared/model/notification.model';
import { IAlbum } from 'app/shared/model/album.model';
import { IComment } from 'app/shared/model/comment.model';
import { IPost } from 'app/shared/model/post.model';
import { ICmessage } from 'app/shared/model/cmessage.model';
import { IFollow } from 'app/shared/model/follow.model';
import { IBlockuser } from 'app/shared/model/blockuser.model';
import { IVtopic } from 'app/shared/model/vtopic.model';
import { IVquestion } from 'app/shared/model/vquestion.model';
import { IVanswer } from 'app/shared/model/vanswer.model';
import { IVthumb } from 'app/shared/model/vthumb.model';
import { IProposal } from 'app/shared/model/proposal.model';
import { IProposalVote } from 'app/shared/model/proposal-vote.model';
import { IInterest } from 'app/shared/model/interest.model';
import { IActivity } from 'app/shared/model/activity.model';
import { ICeleb } from 'app/shared/model/celeb.model';

export interface IAppuser {
  id?: number;
  creationDate?: Moment;
  assignedVotesPoints?: number;
  appprofile?: IAppprofile;
  appphoto?: IAppphoto;
  community?: ICommunity;
  blog?: IBlog;
  notification?: INotification;
  album?: IAlbum;
  comment?: IComment;
  post?: IPost;
  sender?: ICmessage;
  receiver?: ICmessage;
  followed?: IFollow;
  following?: IFollow;
  blockeduser?: IBlockuser;
  blockinguser?: IBlockuser;
  vtopic?: IVtopic;
  vquestion?: IVquestion;
  vanswer?: IVanswer;
  vthumb?: IVthumb;
  proposal?: IProposal;
  proposalVote?: IProposalVote;
  interests?: IInterest[];
  activities?: IActivity[];
  celebs?: ICeleb[];
}

export class Appuser implements IAppuser {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public assignedVotesPoints?: number,
    public appprofile?: IAppprofile,
    public appphoto?: IAppphoto,
    public community?: ICommunity,
    public blog?: IBlog,
    public notification?: INotification,
    public album?: IAlbum,
    public comment?: IComment,
    public post?: IPost,
    public sender?: ICmessage,
    public receiver?: ICmessage,
    public followed?: IFollow,
    public following?: IFollow,
    public blockeduser?: IBlockuser,
    public blockinguser?: IBlockuser,
    public vtopic?: IVtopic,
    public vquestion?: IVquestion,
    public vanswer?: IVanswer,
    public vthumb?: IVthumb,
    public proposal?: IProposal,
    public proposalVote?: IProposalVote,
    public interests?: IInterest[],
    public activities?: IActivity[],
    public celebs?: ICeleb[]
  ) {}
}
