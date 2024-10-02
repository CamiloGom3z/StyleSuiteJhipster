import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImagenDetailComponent } from './imagen-detail.component';

describe('Imagen Management Detail Component', () => {
  let comp: ImagenDetailComponent;
  let fixture: ComponentFixture<ImagenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImagenDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ imagen: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ImagenDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImagenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load imagen on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.imagen).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
