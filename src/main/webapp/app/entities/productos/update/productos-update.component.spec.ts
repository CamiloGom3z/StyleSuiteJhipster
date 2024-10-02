import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductosService } from '../service/productos.service';
import { IProductos, Productos } from '../productos.model';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';
import { CategoriaProductoService } from 'app/entities/categoria-producto/service/categoria-producto.service';

import { ProductosUpdateComponent } from './productos-update.component';

describe('Productos Management Update Component', () => {
  let comp: ProductosUpdateComponent;
  let fixture: ComponentFixture<ProductosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productosService: ProductosService;
  let categoriaProductoService: CategoriaProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductosUpdateComponent],
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
      .overrideTemplate(ProductosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productosService = TestBed.inject(ProductosService);
    categoriaProductoService = TestBed.inject(CategoriaProductoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CategoriaProducto query and add missing value', () => {
      const productos: IProductos = { id: 456 };
      const categoriaProducto: ICategoriaProducto = { id: 34193 };
      productos.categoriaProducto = categoriaProducto;

      const categoriaProductoCollection: ICategoriaProducto[] = [{ id: 80561 }];
      jest.spyOn(categoriaProductoService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaProductoCollection })));
      const additionalCategoriaProductos = [categoriaProducto];
      const expectedCollection: ICategoriaProducto[] = [...additionalCategoriaProductos, ...categoriaProductoCollection];
      jest.spyOn(categoriaProductoService, 'addCategoriaProductoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productos });
      comp.ngOnInit();

      expect(categoriaProductoService.query).toHaveBeenCalled();
      expect(categoriaProductoService.addCategoriaProductoToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaProductoCollection,
        ...additionalCategoriaProductos
      );
      expect(comp.categoriaProductosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productos: IProductos = { id: 456 };
      const categoriaProducto: ICategoriaProducto = { id: 32521 };
      productos.categoriaProducto = categoriaProducto;

      activatedRoute.data = of({ productos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productos));
      expect(comp.categoriaProductosSharedCollection).toContain(categoriaProducto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Productos>>();
      const productos = { id: 123 };
      jest.spyOn(productosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productosService.update).toHaveBeenCalledWith(productos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Productos>>();
      const productos = new Productos();
      jest.spyOn(productosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productos }));
      saveSubject.complete();

      // THEN
      expect(productosService.create).toHaveBeenCalledWith(productos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Productos>>();
      const productos = { id: 123 };
      jest.spyOn(productosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productosService.update).toHaveBeenCalledWith(productos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoriaProductoById', () => {
      it('Should return tracked CategoriaProducto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoriaProductoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
