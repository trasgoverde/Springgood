import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICalbum } from 'app/shared/model/calbum.model';

type EntityResponseType = HttpResponse<ICalbum>;
type EntityArrayResponseType = HttpResponse<ICalbum[]>;

@Injectable({ providedIn: 'root' })
export class CalbumService {
  public resourceUrl = SERVER_API_URL + 'api/calbums';

  constructor(protected http: HttpClient) {}

  create(calbum: ICalbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calbum);
    return this.http
      .post<ICalbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calbum: ICalbum): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calbum);
    return this.http
      .put<ICalbum>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalbum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalbum[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(calbum: ICalbum): ICalbum {
    const copy: ICalbum = Object.assign({}, calbum, {
      creationDate: calbum.creationDate && calbum.creationDate.isValid() ? calbum.creationDate.toJSON() : undefined,
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
      res.body.forEach((calbum: ICalbum) => {
        calbum.creationDate = calbum.creationDate ? moment(calbum.creationDate) : undefined;
      });
    }
    return res;
  }
}
