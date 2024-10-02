import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoServcio, TipoServcio } from '../tipo-servcio.model';

import { TipoServcioService } from './tipo-servcio.service';

describe('TipoServcio Service', () => {
  let service: TipoServcioService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoServcio;
  let expectedResult: ITipoServcio | ITipoServcio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoServcioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      valorTipoServicio: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TipoServcio', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoServcio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoServcio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          valorTipoServicio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoServcio', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new TipoServcio()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoServcio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          valorTipoServicio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TipoServcio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoServcioToCollectionIfMissing', () => {
      it('should add a TipoServcio to an empty array', () => {
        const tipoServcio: ITipoServcio = { id: 123 };
        expectedResult = service.addTipoServcioToCollectionIfMissing([], tipoServcio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoServcio);
      });

      it('should not add a TipoServcio to an array that contains it', () => {
        const tipoServcio: ITipoServcio = { id: 123 };
        const tipoServcioCollection: ITipoServcio[] = [
          {
            ...tipoServcio,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoServcioToCollectionIfMissing(tipoServcioCollection, tipoServcio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoServcio to an array that doesn't contain it", () => {
        const tipoServcio: ITipoServcio = { id: 123 };
        const tipoServcioCollection: ITipoServcio[] = [{ id: 456 }];
        expectedResult = service.addTipoServcioToCollectionIfMissing(tipoServcioCollection, tipoServcio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoServcio);
      });

      it('should add only unique TipoServcio to an array', () => {
        const tipoServcioArray: ITipoServcio[] = [{ id: 123 }, { id: 456 }, { id: 66278 }];
        const tipoServcioCollection: ITipoServcio[] = [{ id: 123 }];
        expectedResult = service.addTipoServcioToCollectionIfMissing(tipoServcioCollection, ...tipoServcioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoServcio: ITipoServcio = { id: 123 };
        const tipoServcio2: ITipoServcio = { id: 456 };
        expectedResult = service.addTipoServcioToCollectionIfMissing([], tipoServcio, tipoServcio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoServcio);
        expect(expectedResult).toContain(tipoServcio2);
      });

      it('should accept null and undefined values', () => {
        const tipoServcio: ITipoServcio = { id: 123 };
        expectedResult = service.addTipoServcioToCollectionIfMissing([], null, tipoServcio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoServcio);
      });

      it('should return initial array if no TipoServcio is added', () => {
        const tipoServcioCollection: ITipoServcio[] = [{ id: 123 }];
        expectedResult = service.addTipoServcioToCollectionIfMissing(tipoServcioCollection, undefined, null);
        expect(expectedResult).toEqual(tipoServcioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
