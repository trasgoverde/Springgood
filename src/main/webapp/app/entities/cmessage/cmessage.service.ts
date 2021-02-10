import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICmessage } from 'app/shared/model/cmessage.model';

type EntityResponseType = HttpResponse<ICmessage>;
type EntityArrayResponseType = HttpResponse<ICmessage[]>;

@Injectable({ providedIn: 'root' })
export class CmessageService {
  public resourceUrl = SERVER_API_URL + 'api/cmessages';

  constructor(protected http: HttpClient) {}

  create(cmessage: ICmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmessage);
    return this.http
      .post<ICmessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cmessage: ICmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cmessage);
    return this.http
      .put<ICmessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICmessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICmessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cmessage: ICmessage): ICmessage {
    const copy: ICmessage = Object.assign({}, cmessage, {
      creationDate: cmessage.creationDate && cmessage.creationDate.isValid() ? cmessage.creationDate.toJSON() : undefined,
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
      res.body.forEach((cmessage: ICmessage) => {
        cmessage.creationDate = cmessage.creationDate ? moment(cmessage.creationDate) : undefined;
      });
    }
    return res;
  }
}
