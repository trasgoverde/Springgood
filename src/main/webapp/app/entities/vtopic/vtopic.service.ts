import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVtopic } from 'app/shared/model/vtopic.model';

type EntityResponseType = HttpResponse<IVtopic>;
type EntityArrayResponseType = HttpResponse<IVtopic[]>;

@Injectable({ providedIn: 'root' })
export class VtopicService {
  public resourceUrl = SERVER_API_URL + 'api/vtopics';

  constructor(protected http: HttpClient) {}

  create(vtopic: IVtopic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vtopic);
    return this.http
      .post<IVtopic>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vtopic: IVtopic): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vtopic);
    return this.http
      .put<IVtopic>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVtopic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVtopic[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(vtopic: IVtopic): IVtopic {
    const copy: IVtopic = Object.assign({}, vtopic, {
      creationDate: vtopic.creationDate && vtopic.creationDate.isValid() ? vtopic.creationDate.toJSON() : undefined,
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
      res.body.forEach((vtopic: IVtopic) => {
        vtopic.creationDate = vtopic.creationDate ? moment(vtopic.creationDate) : undefined;
      });
    }
    return res;
  }
}
