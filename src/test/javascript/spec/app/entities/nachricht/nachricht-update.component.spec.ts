import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { NachrichtUpdateComponent } from 'app/entities/nachricht/nachricht-update.component';
import { NachrichtService } from 'app/entities/nachricht/nachricht.service';
import { Nachricht } from 'app/shared/model/nachricht.model';

describe('Component Tests', () => {
  describe('Nachricht Management Update Component', () => {
    let comp: NachrichtUpdateComponent;
    let fixture: ComponentFixture<NachrichtUpdateComponent>;
    let service: NachrichtService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [NachrichtUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(NachrichtUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NachrichtUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NachrichtService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Nachricht(123);
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
        const entity = new Nachricht();
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
