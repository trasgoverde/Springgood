import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SpringgoodTestModule } from '../../../test.module';
import { BmessageUpdateComponent } from 'app/entities/bmessage/bmessage-update.component';
import { BmessageService } from 'app/entities/bmessage/bmessage.service';
import { Bmessage } from 'app/shared/model/bmessage.model';

describe('Component Tests', () => {
  describe('Bmessage Management Update Component', () => {
    let comp: BmessageUpdateComponent;
    let fixture: ComponentFixture<BmessageUpdateComponent>;
    let service: BmessageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpringgoodTestModule],
        declarations: [BmessageUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BmessageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BmessageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BmessageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Bmessage(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Bmessage();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
