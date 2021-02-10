import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBmessage } from 'app/shared/model/bmessage.model';

@Component({
  selector: 'jhi-bmessage-detail',
  templateUrl: './bmessage-detail.component.html',
})
export class BmessageDetailComponent implements OnInit {
  bmessage: IBmessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bmessage }) => (this.bmessage = bmessage));
  }

  previousState(): void {
    window.history.back();
  }
}
