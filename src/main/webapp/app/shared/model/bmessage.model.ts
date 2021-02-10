import { Moment } from 'moment';

export interface IBmessage {
  id?: number;
  creationDate?: Moment;
  bmessageText?: string;
  isDelivered?: boolean;
}

export class Bmessage implements IBmessage {
  constructor(public id?: number, public creationDate?: Moment, public bmessageText?: string, public isDelivered?: boolean) {
    this.isDelivered = this.isDelivered || false;
  }
}
