import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { NotificationReason } from 'app/shared/model/enumerations/notification-reason.model';

export interface INotification {
  id?: number;
  creationDate?: Moment;
  notificationDate?: Moment;
  notificationReason?: NotificationReason;
  notificationText?: string;
  isDelivered?: boolean;
  appusers?: IAppuser[];
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public notificationDate?: Moment,
    public notificationReason?: NotificationReason,
    public notificationText?: string,
    public isDelivered?: boolean,
    public appusers?: IAppuser[]
  ) {
    this.isDelivered = this.isDelivered || false;
  }
}
