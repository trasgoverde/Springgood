import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBmessage, Bmessage } from 'app/shared/model/bmessage.model';
import { BmessageService } from './bmessage.service';
import { BmessageComponent } from './bmessage.component';
import { BmessageDetailComponent } from './bmessage-detail.component';
import { BmessageUpdateComponent } from './bmessage-update.component';

@Injectable({ providedIn: 'root' })
export class BmessageResolve implements Resolve<IBmessage> {
  constructor(private service: BmessageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBmessage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bmessage: HttpResponse<Bmessage>) => {
          if (bmessage.body) {
            return of(bmessage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bmessage());
  }
}

export const bmessageRoute: Routes = [
  {
    path: '',
    component: BmessageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.bmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BmessageDetailComponent,
    resolve: {
      bmessage: BmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.bmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BmessageUpdateComponent,
    resolve: {
      bmessage: BmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.bmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BmessageUpdateComponent,
    resolve: {
      bmessage: BmessageResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.bmessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
