import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductos, Productos } from '../productos.model';

import { ProductosService } from './productos.service';

describe('Productos Service', () => {
  let service: ProductosService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductos;
  let expectedResult: IProductos | IProductos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductosService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      precio: 0,
      cantidad: 0,
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

    it('should create a Productos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Productos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Productos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          precio: 1,
          cantidad: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Productos', () => {
      const patchObject = Object.assign(
        {
          precio: 1,
        },
        new Productos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Productos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          precio: 1,
          cantidad: 1,
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

    it('should delete a Productos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductosToCollectionIfMissing', () => {
      it('should add a Productos to an empty array', () => {
        const productos: IProductos = { id: 123 };
        expectedResult = service.addProductosToCollectionIfMissing([], productos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productos);
      });

      it('should not add a Productos to an array that contains it', () => {
        const productos: IProductos = { id: 123 };
        const productosCollection: IProductos[] = [
          {
            ...productos,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, productos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Productos to an array that doesn't contain it", () => {
        const productos: IProductos = { id: 123 };
        const productosCollection: IProductos[] = [{ id: 456 }];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, productos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productos);
      });

      it('should add only unique Productos to an array', () => {
        const productosArray: IProductos[] = [{ id: 123 }, { id: 456 }, { id: 20761 }];
        const productosCollection: IProductos[] = [{ id: 123 }];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, ...productosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productos: IProductos = { id: 123 };
        const productos2: IProductos = { id: 456 };
        expectedResult = service.addProductosToCollectionIfMissing([], productos, productos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productos);
        expect(expectedResult).toContain(productos2);
      });

      it('should accept null and undefined values', () => {
        const productos: IProductos = { id: 123 };
        expectedResult = service.addProductosToCollectionIfMissing([], null, productos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productos);
      });

      it('should return initial array if no Productos is added', () => {
        const productosCollection: IProductos[] = [{ id: 123 }];
        expectedResult = service.addProductosToCollectionIfMissing(productosCollection, undefined, null);
        expect(expectedResult).toEqual(productosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
