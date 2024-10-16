import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICargo, Cargo } from '../cargo.model';

import { CargoService } from './cargo.service';

describe('Cargo Service', () => {
  let service: CargoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICargo;
  let expectedResult: ICargo | ICargo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CargoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 0,
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

    it('should create a Cargo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cargo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cargo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cargo', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          descripcion: 1,
        },
        new Cargo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cargo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 1,
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

    it('should delete a Cargo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCargoToCollectionIfMissing', () => {
      it('should add a Cargo to an empty array', () => {
        const cargo: ICargo = { id: 123 };
        expectedResult = service.addCargoToCollectionIfMissing([], cargo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cargo);
      });

      it('should not add a Cargo to an array that contains it', () => {
        const cargo: ICargo = { id: 123 };
        const cargoCollection: ICargo[] = [
          {
            ...cargo,
          },
          { id: 456 },
        ];
        expectedResult = service.addCargoToCollectionIfMissing(cargoCollection, cargo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cargo to an array that doesn't contain it", () => {
        const cargo: ICargo = { id: 123 };
        const cargoCollection: ICargo[] = [{ id: 456 }];
        expectedResult = service.addCargoToCollectionIfMissing(cargoCollection, cargo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cargo);
      });

      it('should add only unique Cargo to an array', () => {
        const cargoArray: ICargo[] = [{ id: 123 }, { id: 456 }, { id: 95577 }];
        const cargoCollection: ICargo[] = [{ id: 123 }];
        expectedResult = service.addCargoToCollectionIfMissing(cargoCollection, ...cargoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cargo: ICargo = { id: 123 };
        const cargo2: ICargo = { id: 456 };
        expectedResult = service.addCargoToCollectionIfMissing([], cargo, cargo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cargo);
        expect(expectedResult).toContain(cargo2);
      });

      it('should accept null and undefined values', () => {
        const cargo: ICargo = { id: 123 };
        expectedResult = service.addCargoToCollectionIfMissing([], null, cargo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cargo);
      });

      it('should return initial array if no Cargo is added', () => {
        const cargoCollection: ICargo[] = [{ id: 123 }];
        expectedResult = service.addCargoToCollectionIfMissing(cargoCollection, undefined, null);
        expect(expectedResult).toEqual(cargoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
