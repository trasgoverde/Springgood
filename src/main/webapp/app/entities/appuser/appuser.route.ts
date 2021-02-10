import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppuser, Appuser } from 'app/shared/model/appuser.model';
import { AppuserService } from './appuser.service';
import { AppuserComponent } from './appuser.component';
import { AppuserDetailComponent } from './appuser-detail.component';
import { AppuserUpdateComponent } from './appuser-update.component';

@Injectable({ providedIn: 'root' })
export class AppuserResolve implements Resolve<IAppuser> {
  constructor(private service: AppuserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppuser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appuser: HttpResponse<Appuser>) => {
          if (appuser.body) {
            return of(appuser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appuser());
  }
}

export const appuserRoute: Routes = [
  {
    path: '',
    component: AppuserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppuserDetailComponent,
    resolve: {
      appuser: AppuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppuserUpdateComponent,
    resolve: {
      appuser: AppuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppuserUpdateComponent,
    resolve: {
      appuser: AppuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
