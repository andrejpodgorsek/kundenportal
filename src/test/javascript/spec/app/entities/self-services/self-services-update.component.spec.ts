import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { SelfServicesUpdateComponent } from 'app/entities/self-services/self-services-update.component';
import { SelfServicesService } from 'app/entities/self-services/self-services.service';
import { SelfServices } from 'app/shared/model/self-services.model';

describe('Component Tests', () => {
  describe('SelfServices Management Update Component', () => {
    let comp: SelfServicesUpdateComponent;
    let fixture: ComponentFixture<SelfServicesUpdateComponent>;
    let service: SelfServicesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [SelfServicesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SelfServicesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SelfServicesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SelfServicesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SelfServices(123);
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
        const entity = new SelfServices();
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
