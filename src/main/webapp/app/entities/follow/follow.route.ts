import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFollow, Follow } from 'app/shared/model/follow.model';
import { FollowService } from './follow.service';
import { FollowComponent } from './follow.component';
import { FollowDetailComponent } from './follow-detail.component';
import { FollowUpdateComponent } from './follow-update.component';

@Injectable({ providedIn: 'root' })
export class FollowResolve implements Resolve<IFollow> {
  constructor(private service: FollowService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFollow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((follow: HttpResponse<Follow>) => {
          if (follow.body) {
            return of(follow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Follow());
  }
}

export const followRoute: Routes = [
  {
    path: '',
    component: FollowComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.follow.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FollowDetailComponent,
    resolve: {
      follow: FollowResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.follow.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FollowUpdateComponent,
    resolve: {
      follow: FollowResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.follow.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FollowUpdateComponent,
    resolve: {
      follow: FollowResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.follow.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
