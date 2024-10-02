import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoServcioDetailComponent } from './tipo-servcio-detail.component';

describe('TipoServcio Management Detail Component', () => {
  let comp: TipoServcioDetailComponent;
  let fixture: ComponentFixture<TipoServcioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoServcioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoServcio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoServcioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoServcioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoServcio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoServcio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
