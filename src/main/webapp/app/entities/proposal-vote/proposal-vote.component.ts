import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposalVote } from 'app/shared/model/proposal-vote.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProposalVoteService } from './proposal-vote.service';
import { ProposalVoteDeleteDialogComponent } from './proposal-vote-delete-dialog.component';

@Component({
  selector: 'jhi-proposal-vote',
  templateUrl: './proposal-vote.component.html',
})
export class ProposalVoteComponent implements OnInit, OnDestroy {
  proposalVotes: IProposalVote[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected proposalVoteService: ProposalVoteService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.proposalVotes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.proposalVoteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IProposalVote[]>) => this.paginateProposalVotes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.proposalVotes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProposalVotes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProposalVote): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProposalVotes(): void {
    this.eventSubscriber = this.eventManager.subscribe('proposalVoteListModification', () => this.reset());
  }

  delete(proposalVote: IProposalVote): void {
    const modalRef = this.modalService.open(ProposalVoteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proposalVote = proposalVote;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProposalVotes(data: IProposalVote[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.proposalVotes.push(data[i]);
      }
    }
  }
}
