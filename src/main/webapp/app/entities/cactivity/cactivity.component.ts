import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICactivity } from 'app/shared/model/cactivity.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CactivityService } from './cactivity.service';
import { CactivityDeleteDialogComponent } from './cactivity-delete-dialog.component';

@Component({
  selector: 'jhi-cactivity',
  templateUrl: './cactivity.component.html',
})
export class CactivityComponent implements OnInit, OnDestroy {
  cactivities: ICactivity[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cactivityService: CactivityService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cactivities = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cactivityService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICactivity[]>) => this.paginateCactivities(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cactivities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCactivities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICactivity): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCactivities(): void {
    this.eventSubscriber = this.eventManager.subscribe('cactivityListModification', () => this.reset());
  }

  delete(cactivity: ICactivity): void {
    const modalRef = this.modalService.open(CactivityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cactivity = cactivity;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCactivities(data: ICactivity[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cactivities.push(data[i]);
      }
    }
  }
}
