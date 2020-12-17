import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { KundeUpdateComponent } from 'app/entities/kunde/kunde-update.component';
import { KundeService } from 'app/entities/kunde/kunde.service';
import { Kunde } from 'app/shared/model/kunde.model';

describe('Component Tests', () => {
  describe('Kunde Management Update Component', () => {
    let comp: KundeUpdateComponent;
    let fixture: ComponentFixture<KundeUpdateComponent>;
    let service: KundeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [KundeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(KundeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KundeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KundeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Kunde(123);
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
        const entity = new Kunde();
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
