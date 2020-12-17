import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDokument, Dokument } from 'app/shared/model/dokument.model';
import { DokumentService } from './dokument.service';
import { DokumentComponent } from './dokument.component';
import { DokumentDetailComponent } from './dokument-detail.component';
import { DokumentUpdateComponent } from './dokument-update.component';

@Injectable({ providedIn: 'root' })
export class DokumentResolve implements Resolve<IDokument> {
  constructor(private service: DokumentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDokument> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dokument: HttpResponse<Dokument>) => {
          if (dokument.body) {
            return of(dokument.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dokument());
  }
}

export const dokumentRoute: Routes = [
  {
    path: '',
    component: DokumentComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.dokument.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DokumentDetailComponent,
    resolve: {
      dokument: DokumentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.dokument.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DokumentUpdateComponent,
    resolve: {
      dokument: DokumentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.dokument.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DokumentUpdateComponent,
    resolve: {
      dokument: DokumentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.dokument.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
