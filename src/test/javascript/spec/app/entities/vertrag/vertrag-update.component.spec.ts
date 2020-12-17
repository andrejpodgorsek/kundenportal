import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { VertragUpdateComponent } from 'app/entities/vertrag/vertrag-update.component';
import { VertragService } from 'app/entities/vertrag/vertrag.service';
import { Vertrag } from 'app/shared/model/vertrag.model';

describe('Component Tests', () => {
  describe('Vertrag Management Update Component', () => {
    let comp: VertragUpdateComponent;
    let fixture: ComponentFixture<VertragUpdateComponent>;
    let service: VertragService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [VertragUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(VertragUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VertragUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VertragService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vertrag(123);
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
        const entity = new Vertrag();
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
