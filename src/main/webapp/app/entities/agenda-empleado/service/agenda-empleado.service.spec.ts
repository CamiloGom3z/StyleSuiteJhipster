import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAgendaEmpleado, AgendaEmpleado } from '../agenda-empleado.model';

import { AgendaEmpleadoService } from './agenda-empleado.service';

describe('AgendaEmpleado Service', () => {
  let service: AgendaEmpleadoService;
  let httpMock: HttpTestingController;
  let elemDefault: IAgendaEmpleado;
  let expectedResult: IAgendaEmpleado | IAgendaEmpleado[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgendaEmpleadoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaInicio: currentDate,
      fechaFin: currentDate,
      disponible: false,
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

    it('should create a AgendaEmpleado', () => {
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

      service.create(new AgendaEmpleado()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgendaEmpleado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          disponible: true,
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

    it('should partial update a AgendaEmpleado', () => {
      const patchObject = Object.assign(
        {
          disponible: true,
        },
        new AgendaEmpleado()
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

    it('should return a list of AgendaEmpleado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaInicio: currentDate.format(DATE_TIME_FORMAT),
          fechaFin: currentDate.format(DATE_TIME_FORMAT),
          disponible: true,
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

    it('should delete a AgendaEmpleado', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAgendaEmpleadoToCollectionIfMissing', () => {
      it('should add a AgendaEmpleado to an empty array', () => {
        const agendaEmpleado: IAgendaEmpleado = { id: 123 };
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing([], agendaEmpleado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendaEmpleado);
      });

      it('should not add a AgendaEmpleado to an array that contains it', () => {
        const agendaEmpleado: IAgendaEmpleado = { id: 123 };
        const agendaEmpleadoCollection: IAgendaEmpleado[] = [
          {
            ...agendaEmpleado,
          },
          { id: 456 },
        ];
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing(agendaEmpleadoCollection, agendaEmpleado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgendaEmpleado to an array that doesn't contain it", () => {
        const agendaEmpleado: IAgendaEmpleado = { id: 123 };
        const agendaEmpleadoCollection: IAgendaEmpleado[] = [{ id: 456 }];
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing(agendaEmpleadoCollection, agendaEmpleado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendaEmpleado);
      });

      it('should add only unique AgendaEmpleado to an array', () => {
        const agendaEmpleadoArray: IAgendaEmpleado[] = [{ id: 123 }, { id: 456 }, { id: 32089 }];
        const agendaEmpleadoCollection: IAgendaEmpleado[] = [{ id: 123 }];
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing(agendaEmpleadoCollection, ...agendaEmpleadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agendaEmpleado: IAgendaEmpleado = { id: 123 };
        const agendaEmpleado2: IAgendaEmpleado = { id: 456 };
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing([], agendaEmpleado, agendaEmpleado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agendaEmpleado);
        expect(expectedResult).toContain(agendaEmpleado2);
      });

      it('should accept null and undefined values', () => {
        const agendaEmpleado: IAgendaEmpleado = { id: 123 };
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing([], null, agendaEmpleado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agendaEmpleado);
      });

      it('should return initial array if no AgendaEmpleado is added', () => {
        const agendaEmpleadoCollection: IAgendaEmpleado[] = [{ id: 123 }];
        expectedResult = service.addAgendaEmpleadoToCollectionIfMissing(agendaEmpleadoCollection, undefined, null);
        expect(expectedResult).toEqual(agendaEmpleadoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
