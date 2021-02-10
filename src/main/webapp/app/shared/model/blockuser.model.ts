import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { ICommunity } from 'app/shared/model/community.model';

export interface IBlockuser {
  id?: number;
  creationDate?: Moment;
  blockedusers?: IAppuser[];
  blockingusers?: IAppuser[];
  cblockeduser?: ICommunity;
  cblockinguser?: ICommunity;
}

export class Blockuser implements IBlockuser {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public blockedusers?: IAppuser[],
    public blockingusers?: IAppuser[],
    public cblockeduser?: ICommunity,
    public cblockinguser?: ICommunity
  ) {}
}
