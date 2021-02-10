import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVanswer } from 'app/shared/model/vanswer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VanswerService } from './vanswer.service';
import { VanswerDeleteDialogComponent } from './vanswer-delete-dialog.component';

@Component({
  selector: 'jhi-vanswer',
  templateUrl: './vanswer.component.html',
})
export class VanswerComponent implements OnInit, OnDestroy {
  vanswers: IVanswer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected vanswerService: VanswerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.vanswers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.vanswerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVanswer[]>) => this.paginateVanswers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.vanswers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVanswers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVanswer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVanswers(): void {
    this.eventSubscriber = this.eventManager.subscribe('vanswerListModification', () => this.reset());
  }

  delete(vanswer: IVanswer): void {
    const modalRef = this.modalService.open(VanswerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vanswer = vanswer;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVanswers(data: IVanswer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.vanswers.push(data[i]);
      }
    }
  }
}
