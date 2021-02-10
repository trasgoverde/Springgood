import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppprofile } from 'app/shared/model/appprofile.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AppprofileService } from './appprofile.service';
import { AppprofileDeleteDialogComponent } from './appprofile-delete-dialog.component';

@Component({
  selector: 'jhi-appprofile',
  templateUrl: './appprofile.component.html',
})
export class AppprofileComponent implements OnInit, OnDestroy {
  appprofiles: IAppprofile[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected appprofileService: AppprofileService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.appprofiles = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.appprofileService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAppprofile[]>) => this.paginateAppprofiles(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.appprofiles = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAppprofiles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppprofile): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAppprofiles(): void {
    this.eventSubscriber = this.eventManager.subscribe('appprofileListModification', () => this.reset());
  }

  delete(appprofile: IAppprofile): void {
    const modalRef = this.modalService.open(AppprofileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appprofile = appprofile;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAppprofiles(data: IAppprofile[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.appprofiles.push(data[i]);
      }
    }
  }
}
