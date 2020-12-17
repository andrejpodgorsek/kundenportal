import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { KundenportalTestModule } from '../../../test.module';
import { NachrichtDetailComponent } from 'app/entities/nachricht/nachricht-detail.component';
import { Nachricht } from 'app/shared/model/nachricht.model';

describe('Component Tests', () => {
  describe('Nachricht Management Detail Component', () => {
    let comp: NachrichtDetailComponent;
    let fixture: ComponentFixture<NachrichtDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ nachricht: new Nachricht(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [NachrichtDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(NachrichtDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NachrichtDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load nachricht on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nachricht).toEqual(jasmine.objectContaining({ id: 123 }));
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
