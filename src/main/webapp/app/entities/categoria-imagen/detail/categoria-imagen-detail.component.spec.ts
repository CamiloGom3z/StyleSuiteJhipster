import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaImagenDetailComponent } from './categoria-imagen-detail.component';

describe('CategoriaImagen Management Detail Component', () => {
  let comp: CategoriaImagenDetailComponent;
  let fixture: ComponentFixture<CategoriaImagenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoriaImagenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ categoriaImagen: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CategoriaImagenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoriaImagenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load categoriaImagen on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.categoriaImagen).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
