import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBmessage } from 'app/shared/model/bmessage.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BmessageService } from './bmessage.service';
import { BmessageDeleteDialogComponent } from './bmessage-delete-dialog.component';

@Component({
  selector: 'jhi-bmessage',
  templateUrl: './bmessage.component.html',
})
export class BmessageComponent implements OnInit, OnDestroy {
  bmessages: IBmessage[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected bmessageService: BmessageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.bmessages = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.bmessageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IBmessage[]>) => this.paginateBmessages(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.bmessages = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBmessages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBmessage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBmessages(): void {
    this.eventSubscriber = this.eventManager.subscribe('bmessageListModification', () => this.reset());
  }

  delete(bmessage: IBmessage): void {
    const modalRef = this.modalService.open(BmessageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bmessage = bmessage;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBmessages(data: IBmessage[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.bmessages.push(data[i]);
      }
    }
  }
}
