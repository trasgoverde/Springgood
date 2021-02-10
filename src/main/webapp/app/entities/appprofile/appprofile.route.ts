import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppprofile, Appprofile } from 'app/shared/model/appprofile.model';
import { AppprofileService } from './appprofile.service';
import { AppprofileComponent } from './appprofile.component';
import { AppprofileDetailComponent } from './appprofile-detail.component';
import { AppprofileUpdateComponent } from './appprofile-update.component';

@Injectable({ providedIn: 'root' })
export class AppprofileResolve implements Resolve<IAppprofile> {
  constructor(private service: AppprofileService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppprofile> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appprofile: HttpResponse<Appprofile>) => {
          if (appprofile.body) {
            return of(appprofile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appprofile());
  }
}

export const appprofileRoute: Routes = [
  {
    path: '',
    component: AppprofileComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appprofile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppprofileDetailComponent,
    resolve: {
      appprofile: AppprofileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appprofile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppprofileUpdateComponent,
    resolve: {
      appprofile: AppprofileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appprofile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppprofileUpdateComponent,
    resolve: {
      appprofile: AppprofileResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appprofile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
