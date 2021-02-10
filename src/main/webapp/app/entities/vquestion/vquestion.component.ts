import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVquestion } from 'app/shared/model/vquestion.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VquestionService } from './vquestion.service';
import { VquestionDeleteDialogComponent } from './vquestion-delete-dialog.component';

@Component({
  selector: 'jhi-vquestion',
  templateUrl: './vquestion.component.html',
})
export class VquestionComponent implements OnInit, OnDestroy {
  vquestions: IVquestion[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected vquestionService: VquestionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.vquestions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.vquestionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVquestion[]>) => this.paginateVquestions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.vquestions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVquestions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVquestion): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVquestions(): void {
    this.eventSubscriber = this.eventManager.subscribe('vquestionListModification', () => this.reset());
  }

  delete(vquestion: IVquestion): void {
    const modalRef = this.modalService.open(VquestionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vquestion = vquestion;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVquestions(data: IVquestion[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.vquestions.push(data[i]);
      }
    }
  }
}
