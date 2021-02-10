import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICceleb } from 'app/shared/model/cceleb.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CcelebService } from './cceleb.service';
import { CcelebDeleteDialogComponent } from './cceleb-delete-dialog.component';

@Component({
  selector: 'jhi-cceleb',
  templateUrl: './cceleb.component.html',
})
export class CcelebComponent implements OnInit, OnDestroy {
  ccelebs: ICceleb[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected ccelebService: CcelebService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.ccelebs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.ccelebService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICceleb[]>) => this.paginateCcelebs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.ccelebs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCcelebs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICceleb): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCcelebs(): void {
    this.eventSubscriber = this.eventManager.subscribe('ccelebListModification', () => this.reset());
  }

  delete(cceleb: ICceleb): void {
    const modalRef = this.modalService.open(CcelebDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cceleb = cceleb;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCcelebs(data: ICceleb[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.ccelebs.push(data[i]);
      }
    }
  }
}
