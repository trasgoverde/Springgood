import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICalbum, Calbum } from 'app/shared/model/calbum.model';
import { CalbumService } from './calbum.service';
import { CalbumComponent } from './calbum.component';
import { CalbumDetailComponent } from './calbum-detail.component';
import { CalbumUpdateComponent } from './calbum-update.component';

@Injectable({ providedIn: 'root' })
export class CalbumResolve implements Resolve<ICalbum> {
  constructor(private service: CalbumService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalbum> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((calbum: HttpResponse<Calbum>) => {
          if (calbum.body) {
            return of(calbum.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Calbum());
  }
}

export const calbumRoute: Routes = [
  {
    path: '',
    component: CalbumComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.calbum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CalbumDetailComponent,
    resolve: {
      calbum: CalbumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.calbum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CalbumUpdateComponent,
    resolve: {
      calbum: CalbumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.calbum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CalbumUpdateComponent,
    resolve: {
      calbum: CalbumResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.calbum.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
