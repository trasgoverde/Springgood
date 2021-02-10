import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { CcelebDetailComponent } from 'app/entities/cceleb/cceleb-detail.component';
import { Cceleb } from 'app/shared/model/cceleb.model';

describe('Component Tests', () => {
  describe('Cceleb Management Detail Component', () => {
    let comp: CcelebDetailComponent;
    let fixture: ComponentFixture<CcelebDetailComponent>;
    const route = ({ data: of({ cceleb: new Cceleb(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [CcelebDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CcelebDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CcelebDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cceleb on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cceleb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
