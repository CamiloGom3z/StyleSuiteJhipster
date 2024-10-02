import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EstablecimientoDetailComponent } from './establecimiento-detail.component';

describe('Establecimiento Management Detail Component', () => {
  let comp: EstablecimientoDetailComponent;
  let fixture: ComponentFixture<EstablecimientoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EstablecimientoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ establecimiento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EstablecimientoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EstablecimientoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load establecimiento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.establecimiento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
