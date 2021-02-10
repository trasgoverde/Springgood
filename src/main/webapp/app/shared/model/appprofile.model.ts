import { Moment } from 'moment';
import { IAppuser } from 'app/shared/model/appuser.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IAppprofile {
  id?: number;
  creationDate?: Moment;
  gender?: Gender;
  phone?: string;
  bio?: string;
  facebook?: string;
  twitter?: string;
  linkedin?: string;
  instagram?: string;
  googlePlus?: string;
  birthdate?: Moment;
  sibblings?: number;
  appuser?: IAppuser;
}

export class Appprofile implements IAppprofile {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public gender?: Gender,
    public phone?: string,
    public bio?: string,
    public facebook?: string,
    public twitter?: string,
    public linkedin?: string,
    public instagram?: string,
    public googlePlus?: string,
    public birthdate?: Moment,
    public sibblings?: number,
    public appuser?: IAppuser
  ) {}
}
