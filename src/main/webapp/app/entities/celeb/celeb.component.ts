import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeleb } from 'app/shared/model/celeb.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CelebService } from './celeb.service';
import { CelebDeleteDialogComponent } from './celeb-delete-dialog.component';

@Component({
  selector: 'jhi-celeb',
  templateUrl: './celeb.component.html',
})
export class CelebComponent implements OnInit, OnDestroy {
  celebs: ICeleb[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected celebService: CelebService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.celebs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.celebService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICeleb[]>) => this.paginateCelebs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.celebs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCelebs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICeleb): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCelebs(): void {
    this.eventSubscriber = this.eventManager.subscribe('celebListModification', () => this.reset());
  }

  delete(celeb: ICeleb): void {
    const modalRef = this.modalService.open(CelebDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.celeb = celeb;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCelebs(data: ICeleb[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.celebs.push(data[i]);
      }
    }
  }
}
