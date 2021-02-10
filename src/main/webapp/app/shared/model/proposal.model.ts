import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IProposalVote } from 'app/shared/model/proposal-vote.model';
import { IPost } from 'app/shared/model/post.model';
import { ProposalType } from 'app/shared/model/enumerations/proposal-type.model';
import { ProposalRole } from 'app/shared/model/enumerations/proposal-role.model';

export interface IProposal {
  id?: number;
  creationDate?: Moment;
  proposalName?: string;
  proposalType?: ProposalType;
  proposalRole?: ProposalRole;
  releaseDate?: Moment;
  isOpen?: boolean;
  isAccepted?: boolean;
  isPaid?: boolean;
  appusers?: IAppuser[];
  proposalVotes?: IProposalVote[];
  post?: IPost;
}

export class Proposal implements IProposal {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public proposalName?: string,
    public proposalType?: ProposalType,
    public proposalRole?: ProposalRole,
    public releaseDate?: Moment,
    public isOpen?: boolean,
    public isAccepted?: boolean,
    public isPaid?: boolean,
    public appusers?: IAppuser[],
    public proposalVotes?: IProposalVote[],
    public post?: IPost
  ) {
    this.isOpen = this.isOpen || false;
    this.isAccepted = this.isAccepted || false;
    this.isPaid = this.isPaid || false;
  }
}
