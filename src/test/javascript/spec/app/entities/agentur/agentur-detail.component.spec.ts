import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { AgenturDetailComponent } from 'app/entities/agentur/agentur-detail.component';
import { Agentur } from 'app/shared/model/agentur.model';

describe('Component Tests', () => {
  describe('Agentur Management Detail Component', () => {
    let comp: AgenturDetailComponent;
    let fixture: ComponentFixture<AgenturDetailComponent>;
    const route = ({ data: of({ agentur: new Agentur(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [AgenturDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AgenturDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AgenturDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load agentur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.agentur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
