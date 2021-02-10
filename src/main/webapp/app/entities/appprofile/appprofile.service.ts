import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAppprofile } from 'app/shared/model/appprofile.model';

type EntityResponseType = HttpResponse<IAppprofile>;
type EntityArrayResponseType = HttpResponse<IAppprofile[]>;

@Injectable({ providedIn: 'root' })
export class AppprofileService {
  public resourceUrl = SERVER_API_URL + 'api/appprofiles';

  constructor(protected http: HttpClient) {}

  create(appprofile: IAppprofile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appprofile);
    return this.http
      .post<IAppprofile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appprofile: IAppprofile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appprofile);
    return this.http
      .put<IAppprofile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppprofile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppprofile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(appprofile: IAppprofile): IAppprofile {
    const copy: IAppprofile = Object.assign({}, appprofile, {
      creationDate: appprofile.creationDate && appprofile.creationDate.isValid() ? appprofile.creationDate.toJSON() : undefined,
      birthdate: appprofile.birthdate && appprofile.birthdate.isValid() ? appprofile.birthdate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
      res.body.birthdate = res.body.birthdate ? moment(res.body.birthdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appprofile: IAppprofile) => {
        appprofile.creationDate = appprofile.creationDate ? moment(appprofile.creationDate) : undefined;
        appprofile.birthdate = appprofile.birthdate ? moment(appprofile.birthdate) : undefined;
      });
    }
    return res;
  }
}
