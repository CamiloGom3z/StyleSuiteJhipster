import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AgendaService } from '../service/agenda.service';
import { IAgenda, Agenda } from '../agenda.model';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

import { AgendaUpdateComponent } from './agenda-update.component';

describe('Agenda Management Update Component', () => {
  let comp: AgendaUpdateComponent;
  let fixture: ComponentFixture<AgendaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agendaService: AgendaService;
  let establecimientoService: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AgendaUpdateComponent],
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
      .overrideTemplate(AgendaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agendaService = TestBed.inject(AgendaService);
    establecimientoService = TestBed.inject(EstablecimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Establecimiento query and add missing value', () => {
      const agenda: IAgenda = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 12524 };
      agenda.establecimiento = establecimiento;

      const establecimientoCollection: IEstablecimiento[] = [{ id: 51443 }];
      jest.spyOn(establecimientoService, 'query').mockReturnValue(of(new HttpResponse({ body: establecimientoCollection })));
      const additionalEstablecimientos = [establecimiento];
      const expectedCollection: IEstablecimiento[] = [...additionalEstablecimientos, ...establecimientoCollection];
      jest.spyOn(establecimientoService, 'addEstablecimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agenda });
      comp.ngOnInit();

      expect(establecimientoService.query).toHaveBeenCalled();
      expect(establecimientoService.addEstablecimientoToCollectionIfMissing).toHaveBeenCalledWith(
        establecimientoCollection,
        ...additionalEstablecimientos
      );
      expect(comp.establecimientosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agenda: IAgenda = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 3198 };
      agenda.establecimiento = establecimiento;

      activatedRoute.data = of({ agenda });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(agenda));
      expect(comp.establecimientosSharedCollection).toContain(establecimiento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agenda>>();
      const agenda = { id: 123 };
      jest.spyOn(agendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agenda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agenda }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(agendaService.update).toHaveBeenCalledWith(agenda);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agenda>>();
      const agenda = new Agenda();
      jest.spyOn(agendaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agenda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agenda }));
      saveSubject.complete();

      // THEN
      expect(agendaService.create).toHaveBeenCalledWith(agenda);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agenda>>();
      const agenda = { id: 123 };
      jest.spyOn(agendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agenda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agendaService.update).toHaveBeenCalledWith(agenda);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEstablecimientoById', () => {
      it('Should return tracked Establecimiento primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEstablecimientoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
