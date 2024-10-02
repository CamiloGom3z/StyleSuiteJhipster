import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategoriaImagen, CategoriaImagen } from '../categoria-imagen.model';

import { CategoriaImagenService } from './categoria-imagen.service';

describe('CategoriaImagen Service', () => {
  let service: CategoriaImagenService;
  let httpMock: HttpTestingController;
  let elemDefault: ICategoriaImagen;
  let expectedResult: ICategoriaImagen | ICategoriaImagen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoriaImagenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
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

    it('should create a CategoriaImagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CategoriaImagen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoriaImagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoriaImagen', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
        },
        new CategoriaImagen()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoriaImagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should delete a CategoriaImagen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCategoriaImagenToCollectionIfMissing', () => {
      it('should add a CategoriaImagen to an empty array', () => {
        const categoriaImagen: ICategoriaImagen = { id: 123 };
        expectedResult = service.addCategoriaImagenToCollectionIfMissing([], categoriaImagen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaImagen);
      });

      it('should not add a CategoriaImagen to an array that contains it', () => {
        const categoriaImagen: ICategoriaImagen = { id: 123 };
        const categoriaImagenCollection: ICategoriaImagen[] = [
          {
            ...categoriaImagen,
          },
          { id: 456 },
        ];
        expectedResult = service.addCategoriaImagenToCollectionIfMissing(categoriaImagenCollection, categoriaImagen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoriaImagen to an array that doesn't contain it", () => {
        const categoriaImagen: ICategoriaImagen = { id: 123 };
        const categoriaImagenCollection: ICategoriaImagen[] = [{ id: 456 }];
        expectedResult = service.addCategoriaImagenToCollectionIfMissing(categoriaImagenCollection, categoriaImagen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaImagen);
      });

      it('should add only unique CategoriaImagen to an array', () => {
        const categoriaImagenArray: ICategoriaImagen[] = [{ id: 123 }, { id: 456 }, { id: 15710 }];
        const categoriaImagenCollection: ICategoriaImagen[] = [{ id: 123 }];
        expectedResult = service.addCategoriaImagenToCollectionIfMissing(categoriaImagenCollection, ...categoriaImagenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoriaImagen: ICategoriaImagen = { id: 123 };
        const categoriaImagen2: ICategoriaImagen = { id: 456 };
        expectedResult = service.addCategoriaImagenToCollectionIfMissing([], categoriaImagen, categoriaImagen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaImagen);
        expect(expectedResult).toContain(categoriaImagen2);
      });

      it('should accept null and undefined values', () => {
        const categoriaImagen: ICategoriaImagen = { id: 123 };
        expectedResult = service.addCategoriaImagenToCollectionIfMissing([], null, categoriaImagen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaImagen);
      });

      it('should return initial array if no CategoriaImagen is added', () => {
        const categoriaImagenCollection: ICategoriaImagen[] = [{ id: 123 }];
        expectedResult = service.addCategoriaImagenToCollectionIfMissing(categoriaImagenCollection, undefined, null);
        expect(expectedResult).toEqual(categoriaImagenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
