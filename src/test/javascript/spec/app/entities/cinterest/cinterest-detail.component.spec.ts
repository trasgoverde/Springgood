import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { CinterestDetailComponent } from 'app/entities/cinterest/cinterest-detail.component';
import { Cinterest } from 'app/shared/model/cinterest.model';

describe('Component Tests', () => {
  describe('Cinterest Management Detail Component', () => {
    let comp: CinterestDetailComponent;
    let fixture: ComponentFixture<CinterestDetailComponent>;
    const route = ({ data: of({ cinterest: new Cinterest(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [CinterestDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CinterestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CinterestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cinterest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cinterest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
