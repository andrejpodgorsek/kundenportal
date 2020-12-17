import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { KundenportalTestModule } from '../../../test.module';
import { DokumentDetailComponent } from 'app/entities/dokument/dokument-detail.component';
import { Dokument } from 'app/shared/model/dokument.model';

describe('Component Tests', () => {
  describe('Dokument Management Detail Component', () => {
    let comp: DokumentDetailComponent;
    let fixture: ComponentFixture<DokumentDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ dokument: new Dokument(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [DokumentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DokumentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DokumentDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load dokument on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dokument).toEqual(jasmine.objectContaining({ id: 123 }));
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
