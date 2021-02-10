import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVanswer, Vanswer } from 'app/shared/model/vanswer.model';
import { VanswerService } from './vanswer.service';
import { VanswerComponent } from './vanswer.component';
import { VanswerDetailComponent } from './vanswer-detail.component';
import { VanswerUpdateComponent } from './vanswer-update.component';

@Injectable({ providedIn: 'root' })
export class VanswerResolve implements Resolve<IVanswer> {
  constructor(private service: VanswerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVanswer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vanswer: HttpResponse<Vanswer>) => {
          if (vanswer.body) {
            return of(vanswer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vanswer());
  }
}

export const vanswerRoute: Routes = [
  {
    path: '',
    component: VanswerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vanswer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VanswerDetailComponent,
    resolve: {
      vanswer: VanswerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vanswer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VanswerUpdateComponent,
    resolve: {
      vanswer: VanswerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vanswer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VanswerUpdateComponent,
    resolve: {
      vanswer: VanswerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vanswer.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
