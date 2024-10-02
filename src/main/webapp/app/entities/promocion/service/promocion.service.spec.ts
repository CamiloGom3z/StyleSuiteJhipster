import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPromocion, Promocion } from '../promocion.model';

import { PromocionService } from './promocion.service';

describe('Promocion Service', () => {
  let service: PromocionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPromocion;
  let expectedResult: IPromocion | IPromocion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PromocionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      porcentajeDescuento: 0,
      fechaInicio: currentDate,
      fechaFin: currentDate,
      tipoPromocion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Promocion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Promocion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Promocion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          porcentajeDescuento: 1,
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          tipoPromocion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Promocion', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
          porcentajeDescuento: 1,
          tipoPromocion: 'BBBBBB',
        },
        new Promocion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Promocion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          porcentajeDescuento: 1,
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          tipoPromocion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaInicio: currentDate,
          fechaFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Promocion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPromocionToCollectionIfMissing', () => {
      it('should add a Promocion to an empty array', () => {
        const promocion: IPromocion = { id: 123 };
        expectedResult = service.addPromocionToCollectionIfMissing([], promocion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promocion);
      });

      it('should not add a Promocion to an array that contains it', () => {
        const promocion: IPromocion = { id: 123 };
        const promocionCollection: IPromocion[] = [
          {
            ...promocion,
          },
          { id: 456 },
        ];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, promocion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Promocion to an array that doesn't contain it", () => {
        const promocion: IPromocion = { id: 123 };
        const promocionCollection: IPromocion[] = [{ id: 456 }];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, promocion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promocion);
      });

      it('should add only unique Promocion to an array', () => {
        const promocionArray: IPromocion[] = [{ id: 123 }, { id: 456 }, { id: 15236 }];
        const promocionCollection: IPromocion[] = [{ id: 123 }];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, ...promocionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promocion: IPromocion = { id: 123 };
        const promocion2: IPromocion = { id: 456 };
        expectedResult = service.addPromocionToCollectionIfMissing([], promocion, promocion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promocion);
        expect(expectedResult).toContain(promocion2);
      });

      it('should accept null and undefined values', () => {
        const promocion: IPromocion = { id: 123 };
        expectedResult = service.addPromocionToCollectionIfMissing([], null, promocion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promocion);
      });

      it('should return initial array if no Promocion is added', () => {
        const promocionCollection: IPromocion[] = [{ id: 123 }];
        expectedResult = service.addPromocionToCollectionIfMissing(promocionCollection, undefined, null);
        expect(expectedResult).toEqual(promocionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
