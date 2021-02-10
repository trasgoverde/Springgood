import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBlockuser, Blockuser } from 'app/shared/model/blockuser.model';
import { BlockuserService } from './blockuser.service';
import { BlockuserComponent } from './blockuser.component';
import { BlockuserDetailComponent } from './blockuser-detail.component';
import { BlockuserUpdateComponent } from './blockuser-update.component';

@Injectable({ providedIn: 'root' })
export class BlockuserResolve implements Resolve<IBlockuser> {
  constructor(private service: BlockuserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBlockuser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((blockuser: HttpResponse<Blockuser>) => {
          if (blockuser.body) {
            return of(blockuser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Blockuser());
  }
}

export const blockuserRoute: Routes = [
  {
    path: '',
    component: BlockuserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.blockuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlockuserDetailComponent,
    resolve: {
      blockuser: BlockuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.blockuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlockuserUpdateComponent,
    resolve: {
      blockuser: BlockuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.blockuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlockuserUpdateComponent,
    resolve: {
      blockuser: BlockuserResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.blockuser.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
