import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { AgenturUpdateComponent } from 'app/entities/agentur/agentur-update.component';
import { AgenturService } from 'app/entities/agentur/agentur.service';
import { Agentur } from 'app/shared/model/agentur.model';

describe('Component Tests', () => {
  describe('Agentur Management Update Component', () => {
    let comp: AgenturUpdateComponent;
    let fixture: ComponentFixture<AgenturUpdateComponent>;
    let service: AgenturService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [AgenturUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AgenturUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AgenturUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AgenturService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Agentur(123);
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
        const entity = new Agentur();
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
