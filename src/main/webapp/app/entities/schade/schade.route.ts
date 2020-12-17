import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISchade, Schade } from 'app/shared/model/schade.model';
import { SchadeService } from './schade.service';
import { SchadeComponent } from './schade.component';
import { SchadeDetailComponent } from './schade-detail.component';
import { SchadeUpdateComponent } from './schade-update.component';

@Injectable({ providedIn: 'root' })
export class SchadeResolve implements Resolve<ISchade> {
  constructor(private service: SchadeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISchade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((schade: HttpResponse<Schade>) => {
          if (schade.body) {
            return of(schade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Schade());
  }
}

export const schadeRoute: Routes = [
  {
    path: '',
    component: SchadeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'kundenportalApp.schade.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SchadeDetailComponent,
    resolve: {
      schade: SchadeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.schade.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SchadeUpdateComponent,
    resolve: {
      schade: SchadeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.schade.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SchadeUpdateComponent,
    resolve: {
      schade: SchadeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'kundenportalApp.schade.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
