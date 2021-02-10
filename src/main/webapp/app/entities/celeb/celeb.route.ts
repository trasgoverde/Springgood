import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICeleb, Celeb } from 'app/shared/model/celeb.model';
import { CelebService } from './celeb.service';
import { CelebComponent } from './celeb.component';
import { CelebDetailComponent } from './celeb-detail.component';
import { CelebUpdateComponent } from './celeb-update.component';

@Injectable({ providedIn: 'root' })
export class CelebResolve implements Resolve<ICeleb> {
  constructor(private service: CelebService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICeleb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((celeb: HttpResponse<Celeb>) => {
          if (celeb.body) {
            return of(celeb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Celeb());
  }
}

export const celebRoute: Routes = [
  {
    path: '',
    component: CelebComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.celeb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CelebDetailComponent,
    resolve: {
      celeb: CelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.celeb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CelebUpdateComponent,
    resolve: {
      celeb: CelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.celeb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CelebUpdateComponent,
    resolve: {
      celeb: CelebResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.celeb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
