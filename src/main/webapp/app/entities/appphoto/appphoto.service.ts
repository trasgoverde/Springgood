import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAppphoto } from 'app/shared/model/appphoto.model';

type EntityResponseType = HttpResponse<IAppphoto>;
type EntityArrayResponseType = HttpResponse<IAppphoto[]>;

@Injectable({ providedIn: 'root' })
export class AppphotoService {
  public resourceUrl = SERVER_API_URL + 'api/appphotos';

  constructor(protected http: HttpClient) {}

  create(appphoto: IAppphoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appphoto);
    return this.http
      .post<IAppphoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appphoto: IAppphoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appphoto);
    return this.http
      .put<IAppphoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppphoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppphoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(appphoto: IAppphoto): IAppphoto {
    const copy: IAppphoto = Object.assign({}, appphoto, {
      creationDate: appphoto.creationDate && appphoto.creationDate.isValid() ? appphoto.creationDate.toJSON() : undefined,
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
      res.body.forEach((appphoto: IAppphoto) => {
        appphoto.creationDate = appphoto.creationDate ? moment(appphoto.creationDate) : undefined;
      });
    }
    return res;
  }
}
