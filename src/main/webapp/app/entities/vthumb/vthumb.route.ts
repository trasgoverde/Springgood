import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVthumb, Vthumb } from 'app/shared/model/vthumb.model';
import { VthumbService } from './vthumb.service';
import { VthumbComponent } from './vthumb.component';
import { VthumbDetailComponent } from './vthumb-detail.component';
import { VthumbUpdateComponent } from './vthumb-update.component';

@Injectable({ providedIn: 'root' })
export class VthumbResolve implements Resolve<IVthumb> {
  constructor(private service: VthumbService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVthumb> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vthumb: HttpResponse<Vthumb>) => {
          if (vthumb.body) {
            return of(vthumb.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vthumb());
  }
}

export const vthumbRoute: Routes = [
  {
    path: '',
    component: VthumbComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vthumb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VthumbDetailComponent,
    resolve: {
      vthumb: VthumbResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vthumb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VthumbUpdateComponent,
    resolve: {
      vthumb: VthumbResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vthumb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VthumbUpdateComponent,
    resolve: {
      vthumb: VthumbResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vthumb.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
