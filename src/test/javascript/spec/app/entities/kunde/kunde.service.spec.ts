import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { KundeService } from 'app/entities/kunde/kunde.service';
import { IKunde, Kunde } from 'app/shared/model/kunde.model';
import { Anrede } from 'app/shared/model/enumerations/anrede.model';

describe('Service Tests', () => {
  describe('Kunde Service', () => {
    let injector: TestBed;
    let service: KundeService;
    let httpMock: HttpTestingController;
    let elemDefault: IKunde;
    let expectedResult: IKunde | IKunde[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(KundeService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Kunde(0, Anrede.HERR, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Kunde', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new Kunde()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Kunde', () => {
        const returnedFromService = Object.assign(
          {
            anrede: 'BBBBBB',
            name: 'BBBBBB',
            vorname: 'BBBBBB',
            email: 'BBBBBB',
            strasse: 'BBBBBB',
            plzort: 'BBBBBB',
            telefonnr: 'BBBBBB',
            iban: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Kunde', () => {
        const returnedFromService = Object.assign(
          {
            anrede: 'BBBBBB',
            name: 'BBBBBB',
            vorname: 'BBBBBB',
            email: 'BBBBBB',
            strasse: 'BBBBBB',
            plzort: 'BBBBBB',
            telefonnr: 'BBBBBB',
            iban: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a Kunde', () => {
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
