import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICinterest, Cinterest } from 'app/shared/model/cinterest.model';
import { CinterestService } from './cinterest.service';
import { CinterestComponent } from './cinterest.component';
import { CinterestDetailComponent } from './cinterest-detail.component';
import { CinterestUpdateComponent } from './cinterest-update.component';

@Injectable({ providedIn: 'root' })
export class CinterestResolve implements Resolve<ICinterest> {
  constructor(private service: CinterestService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICinterest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cinterest: HttpResponse<Cinterest>) => {
          if (cinterest.body) {
            return of(cinterest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cinterest());
  }
}

export const cinterestRoute: Routes = [
  {
    path: '',
    component: CinterestComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cinterest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CinterestDetailComponent,
    resolve: {
      cinterest: CinterestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cinterest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CinterestUpdateComponent,
    resolve: {
      cinterest: CinterestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cinterest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CinterestUpdateComponent,
    resolve: {
      cinterest: CinterestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cinterest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
