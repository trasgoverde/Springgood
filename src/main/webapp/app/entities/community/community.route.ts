import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICommunity, Community } from 'app/shared/model/community.model';
import { CommunityService } from './community.service';
import { CommunityComponent } from './community.component';
import { CommunityDetailComponent } from './community-detail.component';
import { CommunityUpdateComponent } from './community-update.component';

@Injectable({ providedIn: 'root' })
export class CommunityResolve implements Resolve<ICommunity> {
  constructor(private service: CommunityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((community: HttpResponse<Community>) => {
          if (community.body) {
            return of(community.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Community());
  }
}

export const communityRoute: Routes = [
  {
    path: '',
    component: CommunityComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.community.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityDetailComponent,
    resolve: {
      community: CommunityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.community.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityUpdateComponent,
    resolve: {
      community: CommunityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.community.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityUpdateComponent,
    resolve: {
      community: CommunityResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.community.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
