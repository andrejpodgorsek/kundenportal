import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KundenportalTestModule } from '../../../test.module';
import { VertragDetailComponent } from 'app/entities/vertrag/vertrag-detail.component';
import { Vertrag } from 'app/shared/model/vertrag.model';

describe('Component Tests', () => {
  describe('Vertrag Management Detail Component', () => {
    let comp: VertragDetailComponent;
    let fixture: ComponentFixture<VertragDetailComponent>;
    const route = ({ data: of({ vertrag: new Vertrag(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [KundenportalTestModule],
        declarations: [VertragDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VertragDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VertragDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vertrag on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vertrag).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
