import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { NachrichtService } from 'app/entities/nachricht/nachricht.service';
import { INachricht, Nachricht } from 'app/shared/model/nachricht.model';
import { NachrichtStatus } from 'app/shared/model/enumerations/nachricht-status.model';

describe('Service Tests', () => {
  describe('Nachricht Service', () => {
    let injector: TestBed;
    let service: NachrichtService;
    let httpMock: HttpTestingController;
    let elemDefault: INachricht;
    let expectedResult: INachricht | INachricht[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(NachrichtService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Nachricht(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'image/png', 'AAAAAAA', NachrichtStatus.GELESEN, currentDate);
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

      it('should create a Nachricht', () => {
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

        service.create(new Nachricht()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Nachricht', () => {
        const returnedFromService = Object.assign(
          {
            an: 'BBBBBB',
            betreff: 'BBBBBB',
            text: 'BBBBBB',
            anhang: 'BBBBBB',
            status: 'BBBBBB',
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

      it('should return a list of Nachricht', () => {
        const returnedFromService = Object.assign(
          {
            an: 'BBBBBB',
            betreff: 'BBBBBB',
            text: 'BBBBBB',
            anhang: 'BBBBBB',
            status: 'BBBBBB',
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

      it('should delete a Nachricht', () => {
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
