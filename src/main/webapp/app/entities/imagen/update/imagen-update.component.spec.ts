import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImagenService } from '../service/imagen.service';
import { IImagen, Imagen } from '../imagen.model';
import { ICategoriaImagen } from 'app/entities/categoria-imagen/categoria-imagen.model';
import { CategoriaImagenService } from 'app/entities/categoria-imagen/service/categoria-imagen.service';

import { ImagenUpdateComponent } from './imagen-update.component';

describe('Imagen Management Update Component', () => {
  let comp: ImagenUpdateComponent;
  let fixture: ComponentFixture<ImagenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imagenService: ImagenService;
  let categoriaImagenService: CategoriaImagenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImagenUpdateComponent],
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
      .overrideTemplate(ImagenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imagenService = TestBed.inject(ImagenService);
    categoriaImagenService = TestBed.inject(CategoriaImagenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CategoriaImagen query and add missing value', () => {
      const imagen: IImagen = { id: 456 };
      const categoriaImagen: ICategoriaImagen = { id: 12459 };
      imagen.categoriaImagen = categoriaImagen;

      const categoriaImagenCollection: ICategoriaImagen[] = [{ id: 41815 }];
      jest.spyOn(categoriaImagenService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaImagenCollection })));
      const additionalCategoriaImagens = [categoriaImagen];
      const expectedCollection: ICategoriaImagen[] = [...additionalCategoriaImagens, ...categoriaImagenCollection];
      jest.spyOn(categoriaImagenService, 'addCategoriaImagenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imagen });
      comp.ngOnInit();

      expect(categoriaImagenService.query).toHaveBeenCalled();
      expect(categoriaImagenService.addCategoriaImagenToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaImagenCollection,
        ...additionalCategoriaImagens
      );
      expect(comp.categoriaImagensSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const imagen: IImagen = { id: 456 };
      const categoriaImagen: ICategoriaImagen = { id: 28651 };
      imagen.categoriaImagen = categoriaImagen;

      activatedRoute.data = of({ imagen });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(imagen));
      expect(comp.categoriaImagensSharedCollection).toContain(categoriaImagen);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Imagen>>();
      const imagen = { id: 123 };
      jest.spyOn(imagenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagen }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(imagenService.update).toHaveBeenCalledWith(imagen);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Imagen>>();
      const imagen = new Imagen();
      jest.spyOn(imagenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagen }));
      saveSubject.complete();

      // THEN
      expect(imagenService.create).toHaveBeenCalledWith(imagen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Imagen>>();
      const imagen = { id: 123 };
      jest.spyOn(imagenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagen });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imagenService.update).toHaveBeenCalledWith(imagen);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoriaImagenById', () => {
      it('Should return tracked CategoriaImagen primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoriaImagenById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
