import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlockuser } from 'app/shared/model/blockuser.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BlockuserService } from './blockuser.service';
import { BlockuserDeleteDialogComponent } from './blockuser-delete-dialog.component';

@Component({
  selector: 'jhi-blockuser',
  templateUrl: './blockuser.component.html',
})
export class BlockuserComponent implements OnInit, OnDestroy {
  blockusers: IBlockuser[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected blockuserService: BlockuserService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.blockusers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.blockuserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IBlockuser[]>) => this.paginateBlockusers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.blockusers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBlockusers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBlockuser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBlockusers(): void {
    this.eventSubscriber = this.eventManager.subscribe('blockuserListModification', () => this.reset());
  }

  delete(blockuser: IBlockuser): void {
    const modalRef = this.modalService.open(BlockuserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.blockuser = blockuser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBlockusers(data: IBlockuser[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.blockusers.push(data[i]);
      }
    }
  }
}
