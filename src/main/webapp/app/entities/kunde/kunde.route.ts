import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IKunde, Kunde } from 'app/shared/model/kunde.model';
import { KundeService } from './kunde.service';
import { KundeComponent } from './kunde.component';
import { KundeDetailComponent } from './kunde-detail.component';
import { KundeUpdateComponent } from './kunde-update.component';

@Injectable({ providedIn: 'root' })
export class KundeResolve implements Resolve<IKunde> {
  constructor(private service: KundeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IKunde> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((kunde: HttpResponse<Kunde>) => {
          if (kunde.body) {
            return of(kunde.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Kunde());
  }
}

export const kundeRoute: Routes = [
  {
    path: '',
    component: KundeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.kunde.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KundeDetailComponent,
    resolve: {
      kunde: KundeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.kunde.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KundeUpdateComponent,
    resolve: {
      kunde: KundeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.kunde.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KundeUpdateComponent,
    resolve: {
      kunde: KundeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.kunde.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
