import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IAgentur } from 'app/shared/model/agentur.model';

type EntityResponseType = HttpResponse<IAgentur>;
type EntityArrayResponseType = HttpResponse<IAgentur[]>;

@Injectable({ providedIn: 'root' })
export class AgenturService {
  public resourceUrl = SERVER_API_URL + 'api/agenturs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/agenturs';

  constructor(protected http: HttpClient) {}

  create(agentur: IAgentur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agentur);
    return this.http
      .post<IAgentur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agentur: IAgentur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agentur);
    return this.http
      .put<IAgentur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgentur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgentur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgentur[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(agentur: IAgentur): IAgentur {
    const copy: IAgentur = Object.assign({}, agentur, {
      created: agentur.created && agentur.created.isValid() ? agentur.created.toJSON() : undefined,
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
      res.body.forEach((agentur: IAgentur) => {
        agentur.created = agentur.created ? moment(agentur.created) : undefined;
      });
    }
    return res;
  }
}
