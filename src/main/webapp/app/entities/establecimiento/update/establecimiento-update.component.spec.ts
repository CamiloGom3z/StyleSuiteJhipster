import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EstablecimientoService } from '../service/establecimiento.service';
import { IEstablecimiento, Establecimiento } from '../establecimiento.model';

import { EstablecimientoUpdateComponent } from './establecimiento-update.component';

describe('Establecimiento Management Update Component', () => {
  let comp: EstablecimientoUpdateComponent;
  let fixture: ComponentFixture<EstablecimientoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let establecimientoService: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EstablecimientoUpdateComponent],
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
      .overrideTemplate(EstablecimientoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstablecimientoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    establecimientoService = TestBed.inject(EstablecimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const establecimiento: IEstablecimiento = { id: 456 };

      activatedRoute.data = of({ establecimiento });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(establecimiento));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Establecimiento>>();
      const establecimiento = { id: 123 };
      jest.spyOn(establecimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establecimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: establecimiento }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(establecimientoService.update).toHaveBeenCalledWith(establecimiento);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Establecimiento>>();
      const establecimiento = new Establecimiento();
      jest.spyOn(establecimientoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establecimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: establecimiento }));
      saveSubject.complete();

      // THEN
      expect(establecimientoService.create).toHaveBeenCalledWith(establecimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Establecimiento>>();
      const establecimiento = { id: 123 };
      jest.spyOn(establecimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ establecimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(establecimientoService.update).toHaveBeenCalledWith(establecimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
