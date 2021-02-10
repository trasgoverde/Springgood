import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProposalVote, ProposalVote } from 'app/shared/model/proposal-vote.model';
import { ProposalVoteService } from './proposal-vote.service';
import { ProposalVoteComponent } from './proposal-vote.component';
import { ProposalVoteDetailComponent } from './proposal-vote-detail.component';
import { ProposalVoteUpdateComponent } from './proposal-vote-update.component';

@Injectable({ providedIn: 'root' })
export class ProposalVoteResolve implements Resolve<IProposalVote> {
  constructor(private service: ProposalVoteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProposalVote> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((proposalVote: HttpResponse<ProposalVote>) => {
          if (proposalVote.body) {
            return of(proposalVote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProposalVote());
  }
}

export const proposalVoteRoute: Routes = [
  {
    path: '',
    component: ProposalVoteComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposalVote.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProposalVoteDetailComponent,
    resolve: {
      proposalVote: ProposalVoteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposalVote.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProposalVoteUpdateComponent,
    resolve: {
      proposalVote: ProposalVoteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposalVote.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProposalVoteUpdateComponent,
    resolve: {
      proposalVote: ProposalVoteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'springgoodApp.proposalVote.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
