import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISelfServices, SelfServices } from 'app/shared/model/self-services.model';
import { SelfServicesService } from './self-services.service';
import { SelfServicesComponent } from './self-services.component';
import { SelfServicesDetailComponent } from './self-services-detail.component';
import { SelfServicesUpdateComponent } from './self-services-update.component';

@Injectable({ providedIn: 'root' })
export class SelfServicesResolve implements Resolve<ISelfServices> {
  constructor(private service: SelfServicesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISelfServices> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((selfServices: HttpResponse<SelfServices>) => {
          if (selfServices.body) {
            return of(selfServices.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SelfServices());
  }
}

export const selfServicesRoute: Routes = [
  {
    path: '',
    component: SelfServicesComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.selfServices.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SelfServicesDetailComponent,
    resolve: {
      selfServices: SelfServicesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.selfServices.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SelfServicesUpdateComponent,
    resolve: {
      selfServices: SelfServicesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.selfServices.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SelfServicesUpdateComponent,
    resolve: {
      selfServices: SelfServicesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.selfServices.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
