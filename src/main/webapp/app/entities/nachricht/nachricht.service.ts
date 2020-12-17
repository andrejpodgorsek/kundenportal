import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { INachricht } from 'app/shared/model/nachricht.model';

type EntityResponseType = HttpResponse<INachricht>;
type EntityArrayResponseType = HttpResponse<INachricht[]>;

@Injectable({ providedIn: 'root' })
export class NachrichtService {
  public resourceUrl = SERVER_API_URL + 'api/nachrichts';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/nachrichts';

  constructor(protected http: HttpClient) {}

  create(nachricht: INachricht): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nachricht);
    return this.http
      .post<INachricht>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nachricht: INachricht): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nachricht);
    return this.http
      .put<INachricht>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INachricht>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INachricht[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INachricht[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(nachricht: INachricht): INachricht {
    const copy: INachricht = Object.assign({}, nachricht, {
      created: nachricht.created && nachricht.created.isValid() ? nachricht.created.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? moment(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((nachricht: INachricht) => {
        nachricht.created = nachricht.created ? moment(nachricht.created) : undefined;
      });
    }
    return res;
  }
}
