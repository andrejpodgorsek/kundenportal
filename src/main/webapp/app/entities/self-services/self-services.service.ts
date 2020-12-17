import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISelfServices } from 'app/shared/model/self-services.model';

type EntityResponseType = HttpResponse<ISelfServices>;
type EntityArrayResponseType = HttpResponse<ISelfServices[]>;

@Injectable({ providedIn: 'root' })
export class SelfServicesService {
  public resourceUrl = SERVER_API_URL + 'api/self-services';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/self-services';

  constructor(protected http: HttpClient) {}

  create(selfServices: ISelfServices): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(selfServices);
    return this.http
      .post<ISelfServices>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(selfServices: ISelfServices): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(selfServices);
    return this.http
      .put<ISelfServices>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISelfServices>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISelfServices[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISelfServices[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(selfServices: ISelfServices): ISelfServices {
    const copy: ISelfServices = Object.assign({}, selfServices, {
      created: selfServices.created && selfServices.created.isValid() ? selfServices.created.toJSON() : undefined,
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
      res.body.forEach((selfServices: ISelfServices) => {
        selfServices.created = selfServices.created ? moment(selfServices.created) : undefined;
      });
    }
    return res;
  }
}
