import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISchade } from 'app/shared/model/schade.model';

type EntityResponseType = HttpResponse<ISchade>;
type EntityArrayResponseType = HttpResponse<ISchade[]>;

@Injectable({ providedIn: 'root' })
export class SchadeService {
  public resourceUrl = SERVER_API_URL + 'api/schades';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/schades';

  constructor(protected http: HttpClient) {}

  create(schade: ISchade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schade);
    return this.http
      .post<ISchade>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(schade: ISchade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schade);
    return this.http
      .put<ISchade>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISchade>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchade[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISchade[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(schade: ISchade): ISchade {
    const copy: ISchade = Object.assign({}, schade, {
      created: schade.created && schade.created.isValid() ? schade.created.toJSON() : undefined,
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
      res.body.forEach((schade: ISchade) => {
        schade.created = schade.created ? moment(schade.created) : undefined;
      });
    }
    return res;
  }
}
