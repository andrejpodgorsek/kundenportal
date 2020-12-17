import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { SchadeUpdateComponent } from 'app/entities/schade/schade-update.component';
import { SchadeService } from 'app/entities/schade/schade.service';
import { Schade } from 'app/shared/model/schade.model';

describe('Component Tests', () => {
  describe('Schade Management Update Component', () => {
    let comp: SchadeUpdateComponent;
    let fixture: ComponentFixture<SchadeUpdateComponent>;
    let service: SchadeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [SchadeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SchadeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchadeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SchadeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Schade(123);
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
        const entity = new Schade();
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
