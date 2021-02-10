import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { IProposal } from 'app/shared/model/proposal.model';

export interface IProposalVote {
  id?: number;
  creationDate?: Moment;
  votePoints?: number;
  appusers?: IAppuser[];
  proposal?: IProposal;
}

export class ProposalVote implements IProposalVote {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public votePoints?: number,
    public appusers?: IAppuser[],
    public proposal?: IProposal
  ) {}
}
