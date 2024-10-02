import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEstablecimiento, Establecimiento } from '../establecimiento.model';

import { EstablecimientoService } from './establecimiento.service';

describe('Establecimiento Service', () => {
  let service: EstablecimientoService;
  let httpMock: HttpTestingController;
  let elemDefault: IEstablecimiento;
  let expectedResult: IEstablecimiento | IEstablecimiento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EstablecimientoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      nit: 0,
      direccion: 'AAAAAAA',
      telefono: 'AAAAAAA',
      correoElectronico: 'AAAAAAA',
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

    it('should create a Establecimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Establecimiento()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Establecimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          nit: 1,
          direccion: 'BBBBBB',
          telefono: 'BBBBBB',
          correoElectronico: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Establecimiento', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          nit: 1,
          direccion: 'BBBBBB',
        },
        new Establecimiento()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Establecimiento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          nit: 1,
          direccion: 'BBBBBB',
          telefono: 'BBBBBB',
          correoElectronico: 'BBBBBB',
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

    it('should delete a Establecimiento', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEstablecimientoToCollectionIfMissing', () => {
      it('should add a Establecimiento to an empty array', () => {
        const establecimiento: IEstablecimiento = { id: 123 };
        expectedResult = service.addEstablecimientoToCollectionIfMissing([], establecimiento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(establecimiento);
      });

      it('should not add a Establecimiento to an array that contains it', () => {
        const establecimiento: IEstablecimiento = { id: 123 };
        const establecimientoCollection: IEstablecimiento[] = [
          {
            ...establecimiento,
          },
          { id: 456 },
        ];
        expectedResult = service.addEstablecimientoToCollectionIfMissing(establecimientoCollection, establecimiento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Establecimiento to an array that doesn't contain it", () => {
        const establecimiento: IEstablecimiento = { id: 123 };
        const establecimientoCollection: IEstablecimiento[] = [{ id: 456 }];
        expectedResult = service.addEstablecimientoToCollectionIfMissing(establecimientoCollection, establecimiento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(establecimiento);
      });

      it('should add only unique Establecimiento to an array', () => {
        const establecimientoArray: IEstablecimiento[] = [{ id: 123 }, { id: 456 }, { id: 45605 }];
        const establecimientoCollection: IEstablecimiento[] = [{ id: 123 }];
        expectedResult = service.addEstablecimientoToCollectionIfMissing(establecimientoCollection, ...establecimientoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const establecimiento: IEstablecimiento = { id: 123 };
        const establecimiento2: IEstablecimiento = { id: 456 };
        expectedResult = service.addEstablecimientoToCollectionIfMissing([], establecimiento, establecimiento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(establecimiento);
        expect(expectedResult).toContain(establecimiento2);
      });

      it('should accept null and undefined values', () => {
        const establecimiento: IEstablecimiento = { id: 123 };
        expectedResult = service.addEstablecimientoToCollectionIfMissing([], null, establecimiento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(establecimiento);
      });

      it('should return initial array if no Establecimiento is added', () => {
        const establecimientoCollection: IEstablecimiento[] = [{ id: 123 }];
        expectedResult = service.addEstablecimientoToCollectionIfMissing(establecimientoCollection, undefined, null);
        expect(expectedResult).toEqual(establecimientoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
