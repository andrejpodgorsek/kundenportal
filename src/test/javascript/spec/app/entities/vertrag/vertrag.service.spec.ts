import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { VertragService } from 'app/entities/vertrag/vertrag.service';
import { IVertrag, Vertrag } from 'app/shared/model/vertrag.model';
import { Sparte } from 'app/shared/model/enumerations/sparte.model';
import { Rhytmus } from 'app/shared/model/enumerations/rhytmus.model';

describe('Service Tests', () => {
  describe('Vertrag Service', () => {
    let injector: TestBed;
    let service: VertragService;
    let httpMock: HttpTestingController;
    let elemDefault: IVertrag;
    let expectedResult: IVertrag | IVertrag[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(VertragService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Vertrag(0, 'AAAAAAA', Sparte.LV, Rhytmus.MONATLICH, currentDate, currentDate, 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            antragsdatum: currentDate.format(DATE_TIME_FORMAT),
            versicherungsbeginn: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vertrag', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            antragsdatum: currentDate.format(DATE_TIME_FORMAT),
            versicherungsbeginn: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            antragsdatum: currentDate,
            versicherungsbeginn: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new Vertrag()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vertrag', () => {
        const returnedFromService = Object.assign(
          {
            vsnr: 'BBBBBB',
            sparte: 'BBBBBB',
            zahlenrhytmus: 'BBBBBB',
            antragsdatum: currentDate.format(DATE_TIME_FORMAT),
            versicherungsbeginn: currentDate.format(DATE_TIME_FORMAT),
            iban: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            antragsdatum: currentDate,
            versicherungsbeginn: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vertrag', () => {
        const returnedFromService = Object.assign(
          {
            vsnr: 'BBBBBB',
            sparte: 'BBBBBB',
            zahlenrhytmus: 'BBBBBB',
            antragsdatum: currentDate.format(DATE_TIME_FORMAT),
            versicherungsbeginn: currentDate.format(DATE_TIME_FORMAT),
            iban: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            antragsdatum: currentDate,
            versicherungsbeginn: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vertrag', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
