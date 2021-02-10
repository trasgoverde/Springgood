import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProposal, Proposal } from 'app/shared/model/proposal.model';
import { ProposalService } from './proposal.service';
import { ProposalComponent } from './proposal.component';
import { ProposalDetailComponent } from './proposal-detail.component';
import { ProposalUpdateComponent } from './proposal-update.component';

@Injectable({ providedIn: 'root' })
export class ProposalResolve implements Resolve<IProposal> {
  constructor(private service: ProposalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProposal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((proposal: HttpResponse<Proposal>) => {
          if (proposal.body) {
            return of(proposal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Proposal());
  }
}

export const proposalRoute: Routes = [
  {
    path: '',
    component: ProposalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposal.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProposalDetailComponent,
    resolve: {
      proposal: ProposalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposal.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposal.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposal.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
