import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IAppphoto } from 'app/shared/model/appphoto.model';

@Component({
  selector: 'jhi-appphoto-detail',
  templateUrl: './appphoto-detail.component.html',
})
export class AppphotoDetailComponent implements OnInit {
  appphoto: IAppphoto | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appphoto }) => (this.appphoto = appphoto));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
