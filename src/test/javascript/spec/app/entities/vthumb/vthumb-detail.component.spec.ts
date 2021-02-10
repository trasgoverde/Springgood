import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { VthumbDetailComponent } from 'app/entities/vthumb/vthumb-detail.component';
import { Vthumb } from 'app/shared/model/vthumb.model';

describe('Component Tests', () => {
  describe('Vthumb Management Detail Component', () => {
    let comp: VthumbDetailComponent;
    let fixture: ComponentFixture<VthumbDetailComponent>;
    const route = ({ data: of({ vthumb: new Vthumb(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [VthumbDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VthumbDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VthumbDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vthumb on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vthumb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
