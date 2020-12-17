import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAgentur, Agentur } from 'app/shared/model/agentur.model';
import { AgenturService } from './agentur.service';
import { AgenturComponent } from './agentur.component';
import { AgenturDetailComponent } from './agentur-detail.component';
import { AgenturUpdateComponent } from './agentur-update.component';

@Injectable({ providedIn: 'root' })
export class AgenturResolve implements Resolve<IAgentur> {
  constructor(private service: AgenturService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgentur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((agentur: HttpResponse<Agentur>) => {
          if (agentur.body) {
            return of(agentur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Agentur());
  }
}

export const agenturRoute: Routes = [
  {
    path: '',
    component: AgenturComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.agentur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgenturDetailComponent,
    resolve: {
      agentur: AgenturResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.agentur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgenturUpdateComponent,
    resolve: {
      agentur: AgenturResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.agentur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgenturUpdateComponent,
    resolve: {
      agentur: AgenturResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.agentur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
