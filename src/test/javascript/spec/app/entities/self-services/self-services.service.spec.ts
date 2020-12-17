import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SelfServicesService } from 'app/entities/self-services/self-services.service';
import { ISelfServices, SelfServices } from 'app/shared/model/self-services.model';
import { ServicesTyp } from 'app/shared/model/enumerations/services-typ.model';
import { ServicesStatus } from 'app/shared/model/enumerations/services-status.model';

describe('Service Tests', () => {
  describe('SelfServices Service', () => {
    let injector: TestBed;
    let service: SelfServicesService;
    let httpMock: HttpTestingController;
    let elemDefault: ISelfServices;
    let expectedResult: ISelfServices | ISelfServices[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SelfServicesService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SelfServices(
        0,
        ServicesTyp.ADRESSDATEN_AENDERN,
        ServicesStatus.GEPLANT,
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        currentDate
      );
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

      it('should create a SelfServices', () => {
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

        service.create(new SelfServices()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SelfServices', () => {
        const returnedFromService = Object.assign(
          {
            serviceTyp: 'BBBBBB',
            status: 'BBBBBB',
            text: 'BBBBBB',
            datei: 'BBBBBB',
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

      it('should return a list of SelfServices', () => {
        const returnedFromService = Object.assign(
          {
            serviceTyp: 'BBBBBB',
            status: 'BBBBBB',
            text: 'BBBBBB',
            datei: 'BBBBBB',
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

      it('should delete a SelfServices', () => {
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
