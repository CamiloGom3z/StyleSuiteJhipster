import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReseniaService } from '../service/resenia.service';
import { IResenia, Resenia } from '../resenia.model';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

import { ReseniaUpdateComponent } from './resenia-update.component';

describe('Resenia Management Update Component', () => {
  let comp: ReseniaUpdateComponent;
  let fixture: ComponentFixture<ReseniaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reseniaService: ReseniaService;
  let servicioService: ServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReseniaUpdateComponent],
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
      .overrideTemplate(ReseniaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReseniaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reseniaService = TestBed.inject(ReseniaService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Servicio query and add missing value', () => {
      const resenia: IResenia = { id: 456 };
      const servicio: IServicio = { id: 2084 };
      resenia.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 8414 }];
      jest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      jest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resenia });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(servicioCollection, ...additionalServicios);
      expect(comp.serviciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resenia: IResenia = { id: 456 };
      const servicio: IServicio = { id: 62901 };
      resenia.servicio = servicio;

      activatedRoute.data = of({ resenia });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(resenia));
      expect(comp.serviciosSharedCollection).toContain(servicio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resenia>>();
      const resenia = { id: 123 };
      jest.spyOn(reseniaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resenia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resenia }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reseniaService.update).toHaveBeenCalledWith(resenia);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resenia>>();
      const resenia = new Resenia();
      jest.spyOn(reseniaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resenia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resenia }));
      saveSubject.complete();

      // THEN
      expect(reseniaService.create).toHaveBeenCalledWith(resenia);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Resenia>>();
      const resenia = { id: 123 };
      jest.spyOn(reseniaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resenia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reseniaService.update).toHaveBeenCalledWith(resenia);
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
});
