import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmpleadoService } from '../service/empleado.service';
import { IEmpleado, Empleado } from '../empleado.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

import { EmpleadoUpdateComponent } from './empleado-update.component';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoService: EmpleadoService;
  let personaService: PersonaService;
  let establecimientoService: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmpleadoUpdateComponent],
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
      .overrideTemplate(EmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoService = TestBed.inject(EmpleadoService);
    personaService = TestBed.inject(PersonaService);
    establecimientoService = TestBed.inject(EstablecimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call persona query and add missing value', () => {
      const empleado: IEmpleado = { id: 456 };
      const persona: IPersona = { id: 30079 };
      empleado.persona = persona;

      const personaCollection: IPersona[] = [{ id: 4338 }];
      jest.spyOn(personaService, 'query').mockReturnValue(of(new HttpResponse({ body: personaCollection })));
      const expectedCollection: IPersona[] = [persona, ...personaCollection];
      jest.spyOn(personaService, 'addPersonaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(personaService.query).toHaveBeenCalled();
      expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(personaCollection, persona);
      expect(comp.personasCollection).toEqual(expectedCollection);
    });

    it('Should call Establecimiento query and add missing value', () => {
      const empleado: IEmpleado = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 23390 };
      empleado.establecimiento = establecimiento;

      const establecimientoCollection: IEstablecimiento[] = [{ id: 47214 }];
      jest.spyOn(establecimientoService, 'query').mockReturnValue(of(new HttpResponse({ body: establecimientoCollection })));
      const additionalEstablecimientos = [establecimiento];
      const expectedCollection: IEstablecimiento[] = [...additionalEstablecimientos, ...establecimientoCollection];
      jest.spyOn(establecimientoService, 'addEstablecimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(establecimientoService.query).toHaveBeenCalled();
      expect(establecimientoService.addEstablecimientoToCollectionIfMissing).toHaveBeenCalledWith(
        establecimientoCollection,
        ...additionalEstablecimientos
      );
      expect(comp.establecimientosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleado: IEmpleado = { id: 456 };
      const persona: IPersona = { id: 75237 };
      empleado.persona = persona;
      const establecimiento: IEstablecimiento = { id: 28819 };
      empleado.establecimiento = establecimiento;

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(empleado));
      expect(comp.personasCollection).toContain(persona);
      expect(comp.establecimientosSharedCollection).toContain(establecimiento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = new Empleado();
      jest.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoService.create).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Empleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalledWith(empleado);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonaById', () => {
      it('Should return tracked Persona primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEstablecimientoById', () => {
      it('Should return tracked Establecimiento primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEstablecimientoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
