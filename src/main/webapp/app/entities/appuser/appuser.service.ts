import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAppuser } from 'app/shared/model/appuser.model';

type EntityResponseType = HttpResponse<IAppuser>;
type EntityArrayResponseType = HttpResponse<IAppuser[]>;

@Injectable({ providedIn: 'root' })
export class AppuserService {
  public resourceUrl = SERVER_API_URL + 'api/appusers';

  constructor(protected http: HttpClient) {}

  create(appuser: IAppuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appuser);
    return this.http
      .post<IAppuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appuser: IAppuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appuser);
    return this.http
      .put<IAppuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppuser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppuser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(appuser: IAppuser): IAppuser {
    const copy: IAppuser = Object.assign({}, appuser, {
      creationDate: appuser.creationDate && appuser.creationDate.isValid() ? appuser.creationDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appuser: IAppuser) => {
        appuser.creationDate = appuser.creationDate ? moment(appuser.creationDate) : undefined;
      });
    }
    return res;
  }
}
