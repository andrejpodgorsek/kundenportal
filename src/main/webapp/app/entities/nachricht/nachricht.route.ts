import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INachricht, Nachricht } from 'app/shared/model/nachricht.model';
import { NachrichtService } from './nachricht.service';
import { NachrichtComponent } from './nachricht.component';
import { NachrichtDetailComponent } from './nachricht-detail.component';
import { NachrichtUpdateComponent } from './nachricht-update.component';

@Injectable({ providedIn: 'root' })
export class NachrichtResolve implements Resolve<INachricht> {
  constructor(private service: NachrichtService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INachricht> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((nachricht: HttpResponse<Nachricht>) => {
          if (nachricht.body) {
            return of(nachricht.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Nachricht());
  }
}

export const nachrichtRoute: Routes = [
  {
    path: '',
    component: NachrichtComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.nachricht.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NachrichtDetailComponent,
    resolve: {
      nachricht: NachrichtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.nachricht.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NachrichtUpdateComponent,
    resolve: {
      nachricht: NachrichtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.nachricht.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NachrichtUpdateComponent,
    resolve: {
      nachricht: NachrichtResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.nachricht.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
