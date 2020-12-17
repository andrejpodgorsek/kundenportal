import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IVertrag } from 'app/shared/model/vertrag.model';

type EntityResponseType = HttpResponse<IVertrag>;
type EntityArrayResponseType = HttpResponse<IVertrag[]>;

@Injectable({ providedIn: 'root' })
export class VertragService {
  public resourceUrl = SERVER_API_URL + 'api/vertrags';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/vertrags';

  constructor(protected http: HttpClient) {}

  create(vertrag: IVertrag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vertrag);
    return this.http
      .post<IVertrag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vertrag: IVertrag): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vertrag);
    return this.http
      .put<IVertrag>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVertrag>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVertrag[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVertrag[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(vertrag: IVertrag): IVertrag {
    const copy: IVertrag = Object.assign({}, vertrag, {
      antragsdatum: vertrag.antragsdatum && vertrag.antragsdatum.isValid() ? vertrag.antragsdatum.toJSON() : undefined,
      versicherungsbeginn:
        vertrag.versicherungsbeginn && vertrag.versicherungsbeginn.isValid() ? vertrag.versicherungsbeginn.toJSON() : undefined,
      created: vertrag.created && vertrag.created.isValid() ? vertrag.created.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.antragsdatum = res.body.antragsdatum ? moment(res.body.antragsdatum) : undefined;
      res.body.versicherungsbeginn = res.body.versicherungsbeginn ? moment(res.body.versicherungsbeginn) : undefined;
      res.body.created = res.body.created ? moment(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vertrag: IVertrag) => {
        vertrag.antragsdatum = vertrag.antragsdatum ? moment(vertrag.antragsdatum) : undefined;
        vertrag.versicherungsbeginn = vertrag.versicherungsbeginn ? moment(vertrag.versicherungsbeginn) : undefined;
        vertrag.created = vertrag.created ? moment(vertrag.created) : undefined;
      });
    }
    return res;
  }
}
