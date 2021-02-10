import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVtopic } from 'app/shared/model/vtopic.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VtopicService } from './vtopic.service';
import { VtopicDeleteDialogComponent } from './vtopic-delete-dialog.component';

@Component({
  selector: 'jhi-vtopic',
  templateUrl: './vtopic.component.html',
})
export class VtopicComponent implements OnInit, OnDestroy {
  vtopics: IVtopic[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected vtopicService: VtopicService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.vtopics = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.vtopicService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVtopic[]>) => this.paginateVtopics(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.vtopics = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVtopics();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVtopic): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVtopics(): void {
    this.eventSubscriber = this.eventManager.subscribe('vtopicListModification', () => this.reset());
  }

  delete(vtopic: IVtopic): void {
    const modalRef = this.modalService.open(VtopicDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vtopic = vtopic;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVtopics(data: IVtopic[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.vtopics.push(data[i]);
      }
    }
  }
}
