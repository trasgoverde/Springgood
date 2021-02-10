import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBmessage } from 'app/shared/model/bmessage.model';

type EntityResponseType = HttpResponse<IBmessage>;
type EntityArrayResponseType = HttpResponse<IBmessage[]>;

@Injectable({ providedIn: 'root' })
export class BmessageService {
  public resourceUrl = SERVER_API_URL + 'api/bmessages';

  constructor(protected http: HttpClient) {}

  create(bmessage: IBmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bmessage);
    return this.http
      .post<IBmessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bmessage: IBmessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bmessage);
    return this.http
      .put<IBmessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBmessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBmessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bmessage: IBmessage): IBmessage {
    const copy: IBmessage = Object.assign({}, bmessage, {
      creationDate: bmessage.creationDate && bmessage.creationDate.isValid() ? bmessage.creationDate.toJSON() : undefined,
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
      res.body.forEach((bmessage: IBmessage) => {
        bmessage.creationDate = bmessage.creationDate ? moment(bmessage.creationDate) : undefined;
      });
    }
    return res;
  }
}
