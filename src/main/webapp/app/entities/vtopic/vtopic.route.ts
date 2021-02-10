import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVtopic, Vtopic } from 'app/shared/model/vtopic.model';
import { VtopicService } from './vtopic.service';
import { VtopicComponent } from './vtopic.component';
import { VtopicDetailComponent } from './vtopic-detail.component';
import { VtopicUpdateComponent } from './vtopic-update.component';

@Injectable({ providedIn: 'root' })
export class VtopicResolve implements Resolve<IVtopic> {
  constructor(private service: VtopicService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVtopic> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vtopic: HttpResponse<Vtopic>) => {
          if (vtopic.body) {
            return of(vtopic.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vtopic());
  }
}

export const vtopicRoute: Routes = [
  {
    path: '',
    component: VtopicComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vtopic.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VtopicDetailComponent,
    resolve: {
      vtopic: VtopicResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vtopic.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VtopicUpdateComponent,
    resolve: {
      vtopic: VtopicResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vtopic.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VtopicUpdateComponent,
    resolve: {
      vtopic: VtopicResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vtopic.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
