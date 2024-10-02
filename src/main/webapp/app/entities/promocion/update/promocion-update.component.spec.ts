import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromocionService } from '../service/promocion.service';
import { IPromocion, Promocion } from '../promocion.model';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

import { PromocionUpdateComponent } from './promocion-update.component';

describe('Promocion Management Update Component', () => {
  let comp: PromocionUpdateComponent;
  let fixture: ComponentFixture<PromocionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promocionService: PromocionService;
  let servicioService: ServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromocionUpdateComponent],
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
      .overrideTemplate(PromocionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromocionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promocionService = TestBed.inject(PromocionService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Servicio query and add missing value', () => {
      const promocion: IPromocion = { id: 456 };
      const servicios: IServicio[] = [{ id: 99483 }];
      promocion.servicios = servicios;

      const servicioCollection: IServicio[] = [{ id: 31944 }];
      jest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [...servicios];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      jest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(servicioCollection, ...additionalServicios);
      expect(comp.serviciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const promocion: IPromocion = { id: 456 };
      const servicios: IServicio = { id: 21553 };
      promocion.servicios = [servicios];

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(promocion));
      expect(comp.serviciosSharedCollection).toContain(servicios);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = { id: 123 };
      jest.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promocion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(promocionService.update).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = new Promocion();
      jest.spyOn(promocionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promocion }));
      saveSubject.complete();

      // THEN
      expect(promocionService.create).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = { id: 123 };
      jest.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promocionService.update).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackServicioById', () => {
      it('Should return tracked Servicio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServicioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedServicio', () => {
      it('Should return option if no Servicio is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedServicio(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Servicio for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedServicio(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Servicio is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedServicio(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
