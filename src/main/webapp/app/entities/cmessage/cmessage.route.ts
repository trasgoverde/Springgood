import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICmessage, Cmessage } from 'app/shared/model/cmessage.model';
import { CmessageService } from './cmessage.service';
import { CmessageComponent } from './cmessage.component';
import { CmessageDetailComponent } from './cmessage-detail.component';
import { CmessageUpdateComponent } from './cmessage-update.component';

@Injectable({ providedIn: 'root' })
export class CmessageResolve implements Resolve<ICmessage> {
  constructor(private service: CmessageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICmessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cmessage: HttpResponse<Cmessage>) => {
          if (cmessage.body) {
            return of(cmessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cmessage());
  }
}

export const cmessageRoute: Routes = [
  {
    path: '',
    component: CmessageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CmessageDetailComponent,
    resolve: {
      cmessage: CmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CmessageUpdateComponent,
    resolve: {
      cmessage: CmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CmessageUpdateComponent,
    resolve: {
      cmessage: CmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.cmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
