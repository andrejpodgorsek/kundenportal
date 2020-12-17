import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { DokumentUpdateComponent } from 'app/entities/dokument/dokument-update.component';
import { DokumentService } from 'app/entities/dokument/dokument.service';
import { Dokument } from 'app/shared/model/dokument.model';

describe('Component Tests', () => {
  describe('Dokument Management Update Component', () => {
    let comp: DokumentUpdateComponent;
    let fixture: ComponentFixture<DokumentUpdateComponent>;
    let service: DokumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [DokumentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DokumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DokumentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DokumentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dokument(123);
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
        const entity = new Dokument();
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
