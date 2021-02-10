import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterest } from 'app/shared/model/interest.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InterestService } from './interest.service';
import { InterestDeleteDialogComponent } from './interest-delete-dialog.component';

@Component({
  selector: 'jhi-interest',
  templateUrl: './interest.component.html',
})
export class InterestComponent implements OnInit, OnDestroy {
  interests: IInterest[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected interestService: InterestService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.interests = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.interestService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IInterest[]>) => this.paginateInterests(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.interests = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterests();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterest): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterests(): void {
    this.eventSubscriber = this.eventManager.subscribe('interestListModification', () => this.reset());
  }

  delete(interest: IInterest): void {
    const modalRef = this.modalService.open(InterestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interest = interest;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInterests(data: IInterest[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.interests.push(data[i]);
      }
    }
  }
}
