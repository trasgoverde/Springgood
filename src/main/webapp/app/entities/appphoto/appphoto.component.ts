import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppphoto } from 'app/shared/model/appphoto.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AppphotoService } from './appphoto.service';
import { AppphotoDeleteDialogComponent } from './appphoto-delete-dialog.component';

@Component({
  selector: 'jhi-appphoto',
  templateUrl: './appphoto.component.html',
})
export class AppphotoComponent implements OnInit, OnDestroy {
  appphotos: IAppphoto[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected appphotoService: AppphotoService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.appphotos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.appphotoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAppphoto[]>) => this.paginateAppphotos(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.appphotos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAppphotos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppphoto): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInAppphotos(): void {
    this.eventSubscriber = this.eventManager.subscribe('appphotoListModification', () => this.reset());
  }

  delete(appphoto: IAppphoto): void {
    const modalRef = this.modalService.open(AppphotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appphoto = appphoto;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAppphotos(data: IAppphoto[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.appphotos.push(data[i]);
      }
    }
  }
}
