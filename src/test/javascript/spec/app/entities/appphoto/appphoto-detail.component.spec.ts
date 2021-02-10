import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { SpringgoodTestModule } from '../../../test.module';
import { AppphotoDetailComponent } from 'app/entities/appphoto/appphoto-detail.component';
import { Appphoto } from 'app/shared/model/appphoto.model';

describe('Component Tests', () => {
  describe('Appphoto Management Detail Component', () => {
    let comp: AppphotoDetailComponent;
    let fixture: ComponentFixture<AppphotoDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ appphoto: new Appphoto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [AppphotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AppphotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppphotoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load appphoto on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appphoto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
