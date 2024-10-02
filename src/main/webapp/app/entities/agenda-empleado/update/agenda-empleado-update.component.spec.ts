import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AgendaEmpleadoService } from '../service/agenda-empleado.service';
import { IAgendaEmpleado, AgendaEmpleado } from '../agenda-empleado.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { AgendaEmpleadoUpdateComponent } from './agenda-empleado-update.component';

describe('AgendaEmpleado Management Update Component', () => {
  let comp: AgendaEmpleadoUpdateComponent;
  let fixture: ComponentFixture<AgendaEmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agendaEmpleadoService: AgendaEmpleadoService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AgendaEmpleadoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AgendaEmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendaEmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agendaEmpleadoService = TestBed.inject(AgendaEmpleadoService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const agendaEmpleado: IAgendaEmpleado = { id: 456 };
      const empleado: IEmpleado = { id: 40265 };
      agendaEmpleado.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 71067 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agendaEmpleado });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(empleadoCollection, ...additionalEmpleados);
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agendaEmpleado: IAgendaEmpleado = { id: 456 };
      const empleado: IEmpleado = { id: 10950 };
      agendaEmpleado.empleado = empleado;

      activatedRoute.data = of({ agendaEmpleado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(agendaEmpleado));
      expect(comp.empleadosSharedCollection).toContain(empleado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgendaEmpleado>>();
      const agendaEmpleado = { id: 123 };
      jest.spyOn(agendaEmpleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaEmpleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendaEmpleado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(agendaEmpleadoService.update).toHaveBeenCalledWith(agendaEmpleado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgendaEmpleado>>();
      const agendaEmpleado = new AgendaEmpleado();
      jest.spyOn(agendaEmpleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaEmpleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agendaEmpleado }));
      saveSubject.complete();

      // THEN
      expect(agendaEmpleadoService.create).toHaveBeenCalledWith(agendaEmpleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AgendaEmpleado>>();
      const agendaEmpleado = { id: 123 };
      jest.spyOn(agendaEmpleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agendaEmpleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agendaEmpleadoService.update).toHaveBeenCalledWith(agendaEmpleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmpleadoById', () => {
      it('Should return tracked Empleado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpleadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
