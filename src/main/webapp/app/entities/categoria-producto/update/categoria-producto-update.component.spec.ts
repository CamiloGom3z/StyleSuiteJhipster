import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaProductoService } from '../service/categoria-producto.service';
import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';

import { CategoriaProductoUpdateComponent } from './categoria-producto-update.component';

describe('CategoriaProducto Management Update Component', () => {
  let comp: CategoriaProductoUpdateComponent;
  let fixture: ComponentFixture<CategoriaProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaProductoService: CategoriaProductoService;
  let establecimientoService: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaProductoUpdateComponent],
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
      .overrideTemplate(CategoriaProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaProductoService = TestBed.inject(CategoriaProductoService);
    establecimientoService = TestBed.inject(EstablecimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Establecimiento query and add missing value', () => {
      const categoriaProducto: ICategoriaProducto = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 950 };
      categoriaProducto.establecimiento = establecimiento;

      const establecimientoCollection: IEstablecimiento[] = [{ id: 76199 }];
      jest.spyOn(establecimientoService, 'query').mockReturnValue(of(new HttpResponse({ body: establecimientoCollection })));
      const additionalEstablecimientos = [establecimiento];
      const expectedCollection: IEstablecimiento[] = [...additionalEstablecimientos, ...establecimientoCollection];
      jest.spyOn(establecimientoService, 'addEstablecimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      expect(establecimientoService.query).toHaveBeenCalled();
      expect(establecimientoService.addEstablecimientoToCollectionIfMissing).toHaveBeenCalledWith(
        establecimientoCollection,
        ...additionalEstablecimientos
      );
      expect(comp.establecimientosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const categoriaProducto: ICategoriaProducto = { id: 456 };
      const establecimiento: IEstablecimiento = { id: 55500 };
      categoriaProducto.establecimiento = establecimiento;

      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(categoriaProducto));
      expect(comp.establecimientosSharedCollection).toContain(establecimiento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = { id: 123 };
      jest.spyOn(categoriaProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaProducto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaProductoService.update).toHaveBeenCalledWith(categoriaProducto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = new CategoriaProducto();
      jest.spyOn(categoriaProductoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaProducto }));
      saveSubject.complete();

      // THEN
      expect(categoriaProductoService.create).toHaveBeenCalledWith(categoriaProducto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = { id: 123 };
      jest.spyOn(categoriaProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaProductoService.update).toHaveBeenCalledWith(categoriaProducto);
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
