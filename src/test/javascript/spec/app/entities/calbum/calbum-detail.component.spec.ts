import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { CalbumDetailComponent } from 'app/entities/calbum/calbum-detail.component';
import { Calbum } from 'app/shared/model/calbum.model';

describe('Component Tests', () => {
  describe('Calbum Management Detail Component', () => {
    let comp: CalbumDetailComponent;
    let fixture: ComponentFixture<CalbumDetailComponent>;
    const route = ({ data: of({ calbum: new Calbum(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [CalbumDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CalbumDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalbumDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load calbum on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calbum).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
