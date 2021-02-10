import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposal } from 'app/shared/model/proposal.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProposalService } from './proposal.service';
import { ProposalDeleteDialogComponent } from './proposal-delete-dialog.component';

@Component({
  selector: 'jhi-proposal',
  templateUrl: './proposal.component.html',
})
export class ProposalComponent implements OnInit, OnDestroy {
  proposals: IProposal[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected proposalService: ProposalService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.proposals = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.proposalService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IProposal[]>) => this.paginateProposals(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.proposals = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProposals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProposal): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProposals(): void {
    this.eventSubscriber = this.eventManager.subscribe('proposalListModification', () => this.reset());
  }

  delete(proposal: IProposal): void {
    const modalRef = this.modalService.open(ProposalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proposal = proposal;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProposals(data: IProposal[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.proposals.push(data[i]);
      }
    }
  }
}
