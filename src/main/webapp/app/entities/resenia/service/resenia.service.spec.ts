import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IResenia, Resenia } from '../resenia.model';

import { ReseniaService } from './resenia.service';

describe('Resenia Service', () => {
  let service: ReseniaService;
  let httpMock: HttpTestingController;
  let elemDefault: IResenia;
  let expectedResult: IResenia | IResenia[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReseniaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      calificacion: 0,
      comentario: 'AAAAAAA',
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Resenia', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Resenia()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resenia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          calificacion: 1,
          comentario: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Resenia', () => {
      const patchObject = Object.assign(
        {
          calificacion: 1,
          comentario: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        new Resenia()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resenia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          calificacion: 1,
          comentario: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Resenia', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReseniaToCollectionIfMissing', () => {
      it('should add a Resenia to an empty array', () => {
        const resenia: IResenia = { id: 123 };
        expectedResult = service.addReseniaToCollectionIfMissing([], resenia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resenia);
      });

      it('should not add a Resenia to an array that contains it', () => {
        const resenia: IResenia = { id: 123 };
        const reseniaCollection: IResenia[] = [
          {
            ...resenia,
          },
          { id: 456 },
        ];
        expectedResult = service.addReseniaToCollectionIfMissing(reseniaCollection, resenia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resenia to an array that doesn't contain it", () => {
        const resenia: IResenia = { id: 123 };
        const reseniaCollection: IResenia[] = [{ id: 456 }];
        expectedResult = service.addReseniaToCollectionIfMissing(reseniaCollection, resenia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resenia);
      });

      it('should add only unique Resenia to an array', () => {
        const reseniaArray: IResenia[] = [{ id: 123 }, { id: 456 }, { id: 23482 }];
        const reseniaCollection: IResenia[] = [{ id: 123 }];
        expectedResult = service.addReseniaToCollectionIfMissing(reseniaCollection, ...reseniaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resenia: IResenia = { id: 123 };
        const resenia2: IResenia = { id: 456 };
        expectedResult = service.addReseniaToCollectionIfMissing([], resenia, resenia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resenia);
        expect(expectedResult).toContain(resenia2);
      });

      it('should accept null and undefined values', () => {
        const resenia: IResenia = { id: 123 };
        expectedResult = service.addReseniaToCollectionIfMissing([], null, resenia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resenia);
      });

      it('should return initial array if no Resenia is added', () => {
        const reseniaCollection: IResenia[] = [{ id: 123 }];
        expectedResult = service.addReseniaToCollectionIfMissing(reseniaCollection, undefined, null);
        expect(expectedResult).toEqual(reseniaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
