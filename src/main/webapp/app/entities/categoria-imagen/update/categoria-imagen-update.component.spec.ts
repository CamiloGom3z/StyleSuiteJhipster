import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaImagenService } from '../service/categoria-imagen.service';
import { ICategoriaImagen, CategoriaImagen } from '../categoria-imagen.model';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

import { CategoriaImagenUpdateComponent } from './categoria-imagen-update.component';

describe('CategoriaImagen Management Update Component', () => {
  let comp: CategoriaImagenUpdateComponent;
  let fixture: ComponentFixture<CategoriaImagenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaImagenService: CategoriaImagenService;
  let establecimientoService: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaImagenUpdateComponent],
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
      .overrideTemplate(CategoriaImagenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaImagenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaImagenService = TestBed.inject(CategoriaImagenService);
    establecimientoService = TestBed.inject(EstablecimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Establecimiento query and add missing value', () => {
      const categoriaImagen: ICategoriaImagen = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 70744 };
      categoriaImagen.establecimiento = establecimiento;

      const establecimientoCollection: IEstablecimiento[] = [{ id: 70339 }];
      jest.spyOn(establecimientoService, 'query').mockReturnValue(of(new HttpResponse({ body: establecimientoCollection })));
      const additionalEstablecimientos = [establecimiento];
      const expectedCollection: IEstablecimiento[] = [...additionalEstablecimientos, ...establecimientoCollection];
      jest.spyOn(establecimientoService, 'addEstablecimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoriaImagen });
      comp.ngOnInit();

      expect(establecimientoService.query).toHaveBeenCalled();
      expect(establecimientoService.addEstablecimientoToCollectionIfMissing).toHaveBeenCalledWith(
        establecimientoCollection,
        ...additionalEstablecimientos
      );
      expect(comp.establecimientosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const categoriaImagen: ICategoriaImagen = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 68001 };
      categoriaImagen.establecimiento = establecimiento;

      activatedRoute.data = of({ categoriaImagen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(categoriaImagen));
      expect(comp.establecimientosSharedCollection).toContain(establecimiento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaImagen>>();
      const categoriaImagen = { id: 123 };
      jest.spyOn(categoriaImagenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaImagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaImagen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaImagenService.update).toHaveBeenCalledWith(categoriaImagen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaImagen>>();
      const categoriaImagen = new CategoriaImagen();
      jest.spyOn(categoriaImagenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaImagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaImagen }));
      saveSubject.complete();

      // THEN
      expect(categoriaImagenService.create).toHaveBeenCalledWith(categoriaImagen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaImagen>>();
      const categoriaImagen = { id: 123 };
      jest.spyOn(categoriaImagenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaImagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaImagenService.update).toHaveBeenCalledWith(categoriaImagen);
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
