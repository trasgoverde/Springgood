import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVquestion, Vquestion } from 'app/shared/model/vquestion.model';
import { VquestionService } from './vquestion.service';
import { VquestionComponent } from './vquestion.component';
import { VquestionDetailComponent } from './vquestion-detail.component';
import { VquestionUpdateComponent } from './vquestion-update.component';

@Injectable({ providedIn: 'root' })
export class VquestionResolve implements Resolve<IVquestion> {
  constructor(private service: VquestionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVquestion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vquestion: HttpResponse<Vquestion>) => {
          if (vquestion.body) {
            return of(vquestion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vquestion());
  }
}

export const vquestionRoute: Routes = [
  {
    path: '',
    component: VquestionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vquestion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VquestionDetailComponent,
    resolve: {
      vquestion: VquestionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vquestion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VquestionUpdateComponent,
    resolve: {
      vquestion: VquestionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vquestion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VquestionUpdateComponent,
    resolve: {
      vquestion: VquestionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.vquestion.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
