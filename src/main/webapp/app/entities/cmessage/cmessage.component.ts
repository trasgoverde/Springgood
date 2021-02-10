import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICmessage } from 'app/shared/model/cmessage.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CmessageService } from './cmessage.service';
import { CmessageDeleteDialogComponent } from './cmessage-delete-dialog.component';

@Component({
  selector: 'jhi-cmessage',
  templateUrl: './cmessage.component.html',
})
export class CmessageComponent implements OnInit, OnDestroy {
  cmessages: ICmessage[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cmessageService: CmessageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cmessages = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cmessageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICmessage[]>) => this.paginateCmessages(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cmessages = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCmessages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICmessage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCmessages(): void {
    this.eventSubscriber = this.eventManager.subscribe('cmessageListModification', () => this.reset());
  }

  delete(cmessage: ICmessage): void {
    const modalRef = this.modalService.open(CmessageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cmessage = cmessage;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCmessages(data: ICmessage[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cmessages.push(data[i]);
      }
    }
  }
}
