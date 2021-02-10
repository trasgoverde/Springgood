import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppuser } from 'app/shared/model/appuser.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AppuserService } from './appuser.service';
import { AppuserDeleteDialogComponent } from './appuser-delete-dialog.component';

@Component({
  selector: 'jhi-appuser',
  templateUrl: './appuser.component.html',
})
export class AppuserComponent implements OnInit, OnDestroy {
  appusers: IAppuser[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected appuserService: AppuserService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.appusers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.appuserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAppuser[]>) => this.paginateAppusers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.appusers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAppusers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppuser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAppusers(): void {
    this.eventSubscriber = this.eventManager.subscribe('appuserListModification', () => this.reset());
  }

  delete(appuser: IAppuser): void {
    const modalRef = this.modalService.open(AppuserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appuser = appuser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAppusers(data: IAppuser[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.appusers.push(data[i]);
      }
    }
  }
}
