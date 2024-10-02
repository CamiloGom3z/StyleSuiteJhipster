import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CitaService } from '../service/cita.service';
import { ICita, Cita } from '../cita.model';
import { IAgendaEmpleado } from 'app/entities/agenda-empleado/agenda-empleado.model';
import { AgendaEmpleadoService } from 'app/entities/agenda-empleado/service/agenda-empleado.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

import { CitaUpdateComponent } from './cita-update.component';

describe('Cita Management Update Component', () => {
  let comp: CitaUpdateComponent;
  let fixture: ComponentFixture<CitaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let citaService: CitaService;
  let agendaEmpleadoService: AgendaEmpleadoService;
  let personaService: PersonaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CitaUpdateComponent],
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
      .overrideTemplate(CitaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CitaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    citaService = TestBed.inject(CitaService);
    agendaEmpleadoService = TestBed.inject(AgendaEmpleadoService);
    personaService = TestBed.inject(PersonaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call agendaEmpleado query and add missing value', () => {
      const cita: ICita = { id: 456 };
      const agendaEmpleado: IAgendaEmpleado = { id: 27110 };
      cita.agendaEmpleado = agendaEmpleado;

      const agendaEmpleadoCollection: IAgendaEmpleado[] = [{ id: 66735 }];
      jest.spyOn(agendaEmpleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: agendaEmpleadoCollection })));
      const expectedCollection: IAgendaEmpleado[] = [agendaEmpleado, ...agendaEmpleadoCollection];
      jest.spyOn(agendaEmpleadoService, 'addAgendaEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(agendaEmpleadoService.query).toHaveBeenCalled();
      expect(agendaEmpleadoService.addAgendaEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(agendaEmpleadoCollection, agendaEmpleado);
      expect(comp.agendaEmpleadosCollection).toEqual(expectedCollection);
    });

    it('Should call Persona query and add missing value', () => {
      const cita: ICita = { id: 456 };
      const cliente: IPersona = { id: 87258 };
      cita.cliente = cliente;

      const personaCollection: IPersona[] = [{ id: 23381 }];
      jest.spyOn(personaService, 'query').mockReturnValue(of(new HttpResponse({ body: personaCollection })));
      const additionalPersonas = [cliente];
      const expectedCollection: IPersona[] = [...additionalPersonas, ...personaCollection];
      jest.spyOn(personaService, 'addPersonaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(personaService.query).toHaveBeenCalled();
      expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(personaCollection, ...additionalPersonas);
      expect(comp.personasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cita: ICita = { id: 456 };
      const agendaEmpleado: IAgendaEmpleado = { id: 26276 };
      cita.agendaEmpleado = agendaEmpleado;
      const cliente: IPersona = { id: 90955 };
      cita.cliente = cliente;

      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cita));
      expect(comp.agendaEmpleadosCollection).toContain(agendaEmpleado);
      expect(comp.personasSharedCollection).toContain(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cita>>();
      const cita = { id: 123 };
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(citaService.update).toHaveBeenCalledWith(cita);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cita>>();
      const cita = new Cita();
      jest.spyOn(citaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cita }));
      saveSubject.complete();

      // THEN
      expect(citaService.create).toHaveBeenCalledWith(cita);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cita>>();
      const cita = { id: 123 };
      jest.spyOn(citaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cita });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(citaService.update).toHaveBeenCalledWith(cita);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAgendaEmpleadoById', () => {
      it('Should return tracked AgendaEmpleado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAgendaEmpleadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPersonaById', () => {
      it('Should return tracked Persona primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
