import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { KundenportalTestModule } from '../../../test.module';
import { SelfServicesDetailComponent } from 'app/entities/self-services/self-services-detail.component';
import { SelfServices } from 'app/shared/model/self-services.model';

describe('Component Tests', () => {
  describe('SelfServices Management Detail Component', () => {
    let comp: SelfServicesDetailComponent;
    let fixture: ComponentFixture<SelfServicesDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ selfServices: new SelfServices(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [SelfServicesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SelfServicesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SelfServicesDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load selfServices on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.selfServices).toEqual(jasmine.objectContaining({ id: 123 }));
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
