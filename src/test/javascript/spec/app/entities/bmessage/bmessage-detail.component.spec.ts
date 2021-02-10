import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { BmessageDetailComponent } from 'app/entities/bmessage/bmessage-detail.component';
import { Bmessage } from 'app/shared/model/bmessage.model';

describe('Component Tests', () => {
  describe('Bmessage Management Detail Component', () => {
    let comp: BmessageDetailComponent;
    let fixture: ComponentFixture<BmessageDetailComponent>;
    const route = ({ data: of({ bmessage: new Bmessage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [BmessageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BmessageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BmessageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bmessage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bmessage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
