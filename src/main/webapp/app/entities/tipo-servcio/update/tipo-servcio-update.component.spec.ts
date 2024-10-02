import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoServcioService } from '../service/tipo-servcio.service';
import { ITipoServcio, TipoServcio } from '../tipo-servcio.model';

import { TipoServcioUpdateComponent } from './tipo-servcio-update.component';

describe('TipoServcio Management Update Component', () => {
  let comp: TipoServcioUpdateComponent;
  let fixture: ComponentFixture<TipoServcioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoServcioService: TipoServcioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoServcioUpdateComponent],
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
      .overrideTemplate(TipoServcioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoServcioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoServcioService = TestBed.inject(TipoServcioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoServcio: ITipoServcio = { id: 456 };

      activatedRoute.data = of({ tipoServcio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoServcio));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoServcio>>();
      const tipoServcio = { id: 123 };
      jest.spyOn(tipoServcioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoServcio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoServcio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoServcioService.update).toHaveBeenCalledWith(tipoServcio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoServcio>>();
      const tipoServcio = new TipoServcio();
      jest.spyOn(tipoServcioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoServcio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoServcio }));
      saveSubject.complete();

      // THEN
      expect(tipoServcioService.create).toHaveBeenCalledWith(tipoServcio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoServcio>>();
      const tipoServcio = { id: 123 };
      jest.spyOn(tipoServcioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoServcio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoServcioService.update).toHaveBeenCalledWith(tipoServcio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
