import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { ICommunity } from 'app/shared/model/community.model';

export interface ICmessage {
  id?: number;
  creationDate?: Moment;
  cmessageText?: string;
  isDelivered?: boolean;
  senders?: IAppuser[];
  receivers?: IAppuser[];
  csender?: ICommunity;
  creceiver?: ICommunity;
}

export class Cmessage implements ICmessage {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public cmessageText?: string,
    public isDelivered?: boolean,
    public senders?: IAppuser[],
    public receivers?: IAppuser[],
    public csender?: ICommunity,
    public creceiver?: ICommunity
  ) {
    this.isDelivered = this.isDelivered || false;
  }
}
