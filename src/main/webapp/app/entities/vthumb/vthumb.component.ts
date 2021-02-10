import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVthumb } from 'app/shared/model/vthumb.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VthumbService } from './vthumb.service';
import { VthumbDeleteDialogComponent } from './vthumb-delete-dialog.component';

@Component({
  selector: 'jhi-vthumb',
  templateUrl: './vthumb.component.html',
})
export class VthumbComponent implements OnInit, OnDestroy {
  vthumbs: IVthumb[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected vthumbService: VthumbService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.vthumbs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.vthumbService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVthumb[]>) => this.paginateVthumbs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.vthumbs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVthumbs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVthumb): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVthumbs(): void {
    this.eventSubscriber = this.eventManager.subscribe('vthumbListModification', () => this.reset());
  }

  delete(vthumb: IVthumb): void {
    const modalRef = this.modalService.open(VthumbDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vthumb = vthumb;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVthumbs(data: IVthumb[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.vthumbs.push(data[i]);
      }
    }
  }
}
