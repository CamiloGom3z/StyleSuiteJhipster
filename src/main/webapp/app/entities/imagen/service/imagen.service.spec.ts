import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImagen, Imagen } from '../imagen.model';

import { ImagenService } from './imagen.service';

describe('Imagen Service', () => {
  let service: ImagenService;
  let httpMock: HttpTestingController;
  let elemDefault: IImagen;
  let expectedResult: IImagen | IImagen[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImagenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      urlImagen: 'AAAAAAA',
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

    it('should create a Imagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Imagen()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Imagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          urlImagen: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Imagen', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
          urlImagen: 'BBBBBB',
        },
        new Imagen()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Imagen', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          urlImagen: 'BBBBBB',
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

    it('should delete a Imagen', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addImagenToCollectionIfMissing', () => {
      it('should add a Imagen to an empty array', () => {
        const imagen: IImagen = { id: 123 };
        expectedResult = service.addImagenToCollectionIfMissing([], imagen);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagen);
      });

      it('should not add a Imagen to an array that contains it', () => {
        const imagen: IImagen = { id: 123 };
        const imagenCollection: IImagen[] = [
          {
            ...imagen,
          },
          { id: 456 },
        ];
        expectedResult = service.addImagenToCollectionIfMissing(imagenCollection, imagen);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Imagen to an array that doesn't contain it", () => {
        const imagen: IImagen = { id: 123 };
        const imagenCollection: IImagen[] = [{ id: 456 }];
        expectedResult = service.addImagenToCollectionIfMissing(imagenCollection, imagen);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagen);
      });

      it('should add only unique Imagen to an array', () => {
        const imagenArray: IImagen[] = [{ id: 123 }, { id: 456 }, { id: 44015 }];
        const imagenCollection: IImagen[] = [{ id: 123 }];
        expectedResult = service.addImagenToCollectionIfMissing(imagenCollection, ...imagenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const imagen: IImagen = { id: 123 };
        const imagen2: IImagen = { id: 456 };
        expectedResult = service.addImagenToCollectionIfMissing([], imagen, imagen2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(imagen);
        expect(expectedResult).toContain(imagen2);
      });

      it('should accept null and undefined values', () => {
        const imagen: IImagen = { id: 123 };
        expectedResult = service.addImagenToCollectionIfMissing([], null, imagen, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(imagen);
      });

      it('should return initial array if no Imagen is added', () => {
        const imagenCollection: IImagen[] = [{ id: 123 }];
        expectedResult = service.addImagenToCollectionIfMissing(imagenCollection, undefined, null);
        expect(expectedResult).toEqual(imagenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
