import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IKunde } from 'app/shared/model/kunde.model';

type EntityResponseType = HttpResponse<IKunde>;
type EntityArrayResponseType = HttpResponse<IKunde[]>;

@Injectable({ providedIn: 'root' })
export class KundeService {
  public resourceUrl = SERVER_API_URL + 'api/kundes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/kundes';

  constructor(protected http: HttpClient) {}

  create(kunde: IKunde): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kunde);
    return this.http
      .post<IKunde>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(kunde: IKunde): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kunde);
    return this.http
      .put<IKunde>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IKunde>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IKunde[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IKunde[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(kunde: IKunde): IKunde {
    const copy: IKunde = Object.assign({}, kunde, {
      created: kunde.created && kunde.created.isValid() ? kunde.created.toJSON() : undefined,
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
      res.body.forEach((kunde: IKunde) => {
        kunde.created = kunde.created ? moment(kunde.created) : undefined;
      });
    }
    return res;
  }
}
