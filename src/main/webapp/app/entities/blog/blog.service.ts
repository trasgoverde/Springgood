import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBlog } from 'app/shared/model/blog.model';

type EntityResponseType = HttpResponse<IBlog>;
type EntityArrayResponseType = HttpResponse<IBlog[]>;

@Injectable({ providedIn: 'root' })
export class BlogService {
  public resourceUrl = SERVER_API_URL + 'api/blogs';

  constructor(protected http: HttpClient) {}

  create(blog: IBlog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blog);
    return this.http
      .post<IBlog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(blog: IBlog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blog);
    return this.http
      .put<IBlog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBlog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBlog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(blog: IBlog): IBlog {
    const copy: IBlog = Object.assign({}, blog, {
      creationDate: blog.creationDate && blog.creationDate.isValid() ? blog.creationDate.toJSON() : undefined,
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
      res.body.forEach((blog: IBlog) => {
        blog.creationDate = blog.creationDate ? moment(blog.creationDate) : undefined;
      });
    }
    return res;
  }
}
