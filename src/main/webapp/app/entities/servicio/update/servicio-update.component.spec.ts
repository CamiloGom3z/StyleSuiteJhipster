import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServicioService } from '../service/servicio.service';
import { IServicio, Servicio } from '../servicio.model';
import { ITipoServcio } from 'app/entities/tipo-servcio/tipo-servcio.model';
import { TipoServcioService } from 'app/entities/tipo-servcio/service/tipo-servcio.service';
import { ICita } from 'app/entities/cita/cita.model';
import { CitaService } from 'app/entities/cita/service/cita.service';

import { ServicioUpdateComponent } from './servicio-update.component';

describe('Servicio Management Update Component', () => {
  let comp: ServicioUpdateComponent;
  let fixture: ComponentFixture<ServicioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let servicioService: ServicioService;
  let tipoServcioService: TipoServcioService;
  let citaService: CitaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ServicioUpdateComponent],
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
      .overrideTemplate(ServicioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServicioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioService = TestBed.inject(ServicioService);
    tipoServcioService = TestBed.inject(TipoServcioService);
    citaService = TestBed.inject(CitaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TipoServcio query and add missing value', () => {
      const servicio: IServicio = { id: 456 };
      const tipoServicio: ITipoServcio = { id: 71304 };
      servicio.tipoServicio = tipoServicio;

      const tipoServcioCollection: ITipoServcio[] = [{ id: 97375 }];
      jest.spyOn(tipoServcioService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoServcioCollection })));
      const additionalTipoServcios = [tipoServicio];
      const expectedCollection: ITipoServcio[] = [...additionalTipoServcios, ...tipoServcioCollection];
      jest.spyOn(tipoServcioService, 'addTipoServcioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(tipoServcioService.query).toHaveBeenCalled();
      expect(tipoServcioService.addTipoServcioToCollectionIfMissing).toHaveBeenCalledWith(tipoServcioCollection, ...additionalTipoServcios);
      expect(comp.tipoServciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cita query and add missing value', () => {
      const servicio: IServicio = { id: 456 };
      const cita: ICita = { id: 33595 };
      servicio.cita = cita;

      const citaCollection: ICita[] = [{ id: 66626 }];
      jest.spyOn(citaService, 'query').mockReturnValue(of(new HttpResponse({ body: citaCollection })));
      const additionalCitas = [cita];
      const expectedCollection: ICita[] = [...additionalCitas, ...citaCollection];
      jest.spyOn(citaService, 'addCitaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(citaService.query).toHaveBeenCalled();
      expect(citaService.addCitaToCollectionIfMissing).toHaveBeenCalledWith(citaCollection, ...additionalCitas);
      expect(comp.citasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const servicio: IServicio = { id: 456 };
      const tipoServicio: ITipoServcio = { id: 55248 };
      servicio.tipoServicio = tipoServicio;
      const cita: ICita = { id: 1135 };
      servicio.cita = cita;

      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(servicio));
      expect(comp.tipoServciosSharedCollection).toContain(tipoServicio);
      expect(comp.citasSharedCollection).toContain(cita);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = { id: 123 };
      jest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servicio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioService.update).toHaveBeenCalledWith(servicio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = new Servicio();
      jest.spyOn(servicioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: servicio }));
      saveSubject.complete();

      // THEN
      expect(servicioService.create).toHaveBeenCalledWith(servicio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Servicio>>();
      const servicio = { id: 123 };
      jest.spyOn(servicioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioService.update).toHaveBeenCalledWith(servicio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoServcioById', () => {
      it('Should return tracked TipoServcio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoServcioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCitaById', () => {
      it('Should return tracked Cita primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCitaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
