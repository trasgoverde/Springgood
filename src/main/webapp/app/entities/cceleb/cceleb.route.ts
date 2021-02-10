import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICceleb, Cceleb } from 'app/shared/model/cceleb.model';
import { CcelebService } from './cceleb.service';
import { CcelebComponent } from './cceleb.component';
import { CcelebDetailComponent } from './cceleb-detail.component';
import { CcelebUpdateComponent } from './cceleb-update.component';

@Injectable({ providedIn: 'root' })
export class CcelebResolve implements Resolve<ICceleb> {
  constructor(private service: CcelebService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICceleb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cceleb: HttpResponse<Cceleb>) => {
          if (cceleb.body) {
            return of(cceleb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cceleb());
  }
}

export const ccelebRoute: Routes = [
  {
    path: '',
    component: CcelebComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cceleb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CcelebDetailComponent,
    resolve: {
      cceleb: CcelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cceleb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CcelebUpdateComponent,
    resolve: {
      cceleb: CcelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cceleb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CcelebUpdateComponent,
    resolve: {
      cceleb: CcelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cceleb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
