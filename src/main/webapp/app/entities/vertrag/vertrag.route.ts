import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVertrag, Vertrag } from 'app/shared/model/vertrag.model';
import { VertragService } from './vertrag.service';
import { VertragComponent } from './vertrag.component';
import { VertragDetailComponent } from './vertrag-detail.component';
import { VertragUpdateComponent } from './vertrag-update.component';

@Injectable({ providedIn: 'root' })
export class VertragResolve implements Resolve<IVertrag> {
  constructor(private service: VertragService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVertrag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vertrag: HttpResponse<Vertrag>) => {
          if (vertrag.body) {
            return of(vertrag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vertrag());
  }
}

export const vertragRoute: Routes = [
  {
    path: '',
    component: VertragComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.vertrag.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VertragDetailComponent,
    resolve: {
      vertrag: VertragResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.vertrag.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VertragUpdateComponent,
    resolve: {
      vertrag: VertragResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.vertrag.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VertragUpdateComponent,
    resolve: {
      vertrag: VertragResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.vertrag.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
