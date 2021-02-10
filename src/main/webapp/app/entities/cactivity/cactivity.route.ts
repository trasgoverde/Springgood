import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICactivity, Cactivity } from 'app/shared/model/cactivity.model';
import { CactivityService } from './cactivity.service';
import { CactivityComponent } from './cactivity.component';
import { CactivityDetailComponent } from './cactivity-detail.component';
import { CactivityUpdateComponent } from './cactivity-update.component';

@Injectable({ providedIn: 'root' })
export class CactivityResolve implements Resolve<ICactivity> {
  constructor(private service: CactivityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICactivity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cactivity: HttpResponse<Cactivity>) => {
          if (cactivity.body) {
            return of(cactivity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cactivity());
  }
}

export const cactivityRoute: Routes = [
  {
    path: '',
    component: CactivityComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cactivity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CactivityDetailComponent,
    resolve: {
      cactivity: CactivityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cactivity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CactivityUpdateComponent,
    resolve: {
      cactivity: CactivityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cactivity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CactivityUpdateComponent,
    resolve: {
      cactivity: CactivityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cactivity.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
