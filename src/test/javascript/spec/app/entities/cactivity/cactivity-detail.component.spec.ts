import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { CactivityDetailComponent } from 'app/entities/cactivity/cactivity-detail.component';
import { Cactivity } from 'app/shared/model/cactivity.model';

describe('Component Tests', () => {
  describe('Cactivity Management Detail Component', () => {
    let comp: CactivityDetailComponent;
    let fixture: ComponentFixture<CactivityDetailComponent>;
    const route = ({ data: of({ cactivity: new Cactivity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [CactivityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CactivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CactivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cactivity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cactivity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
