import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { KundeDetailComponent } from 'app/entities/kunde/kunde-detail.component';
import { Kunde } from 'app/shared/model/kunde.model';

describe('Component Tests', () => {
  describe('Kunde Management Detail Component', () => {
    let comp: KundeDetailComponent;
    let fixture: ComponentFixture<KundeDetailComponent>;
    const route = ({ data: of({ kunde: new Kunde(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [KundeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(KundeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(KundeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load kunde on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.kunde).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
