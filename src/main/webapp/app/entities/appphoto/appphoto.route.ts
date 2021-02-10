import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppphoto, Appphoto } from 'app/shared/model/appphoto.model';
import { AppphotoService } from './appphoto.service';
import { AppphotoComponent } from './appphoto.component';
import { AppphotoDetailComponent } from './appphoto-detail.component';
import { AppphotoUpdateComponent } from './appphoto-update.component';

@Injectable({ providedIn: 'root' })
export class AppphotoResolve implements Resolve<IAppphoto> {
  constructor(private service: AppphotoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppphoto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appphoto: HttpResponse<Appphoto>) => {
          if (appphoto.body) {
            return of(appphoto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Appphoto());
  }
}

export const appphotoRoute: Routes = [
  {
    path: '',
    component: AppphotoComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appphoto.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppphotoDetailComponent,
    resolve: {
      appphoto: AppphotoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appphoto.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppphotoUpdateComponent,
    resolve: {
      appphoto: AppphotoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appphoto.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppphotoUpdateComponent,
    resolve: {
      appphoto: AppphotoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.appphoto.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
