import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReseniaDetailComponent } from './resenia-detail.component';

describe('Resenia Management Detail Component', () => {
  let comp: ReseniaDetailComponent;
  let fixture: ComponentFixture<ReseniaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReseniaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resenia: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReseniaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReseniaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resenia on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resenia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
