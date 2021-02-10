import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFollow } from 'app/shared/model/follow.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FollowService } from './follow.service';
import { FollowDeleteDialogComponent } from './follow-delete-dialog.component';

@Component({
  selector: 'jhi-follow',
  templateUrl: './follow.component.html',
})
export class FollowComponent implements OnInit, OnDestroy {
  follows: IFollow[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected followService: FollowService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.follows = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.followService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFollow[]>) => this.paginateFollows(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.follows = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFollows();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFollow): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFollows(): void {
    this.eventSubscriber = this.eventManager.subscribe('followListModification', () => this.reset());
  }

  delete(follow: IFollow): void {
    const modalRef = this.modalService.open(FollowDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.follow = follow;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFollows(data: IFollow[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.follows.push(data[i]);
      }
    }
  }
}
