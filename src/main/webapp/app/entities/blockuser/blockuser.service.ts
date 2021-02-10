import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBlockuser } from 'app/shared/model/blockuser.model';

type EntityResponseType = HttpResponse<IBlockuser>;
type EntityArrayResponseType = HttpResponse<IBlockuser[]>;

@Injectable({ providedIn: 'root' })
export class BlockuserService {
  public resourceUrl = SERVER_API_URL + 'api/blockusers';

  constructor(protected http: HttpClient) {}

  create(blockuser: IBlockuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blockuser);
    return this.http
      .post<IBlockuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(blockuser: IBlockuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blockuser);
    return this.http
      .put<IBlockuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBlockuser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBlockuser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(blockuser: IBlockuser): IBlockuser {
    const copy: IBlockuser = Object.assign({}, blockuser, {
      creationDate: blockuser.creationDate && blockuser.creationDate.isValid() ? blockuser.creationDate.toJSON() : undefined,
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
      res.body.forEach((blockuser: IBlockuser) => {
        blockuser.creationDate = blockuser.creationDate ? moment(blockuser.creationDate) : undefined;
      });
    }
    return res;
  }
}
