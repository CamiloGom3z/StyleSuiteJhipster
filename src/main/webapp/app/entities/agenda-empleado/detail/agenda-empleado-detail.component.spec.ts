import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AgendaEmpleadoDetailComponent } from './agenda-empleado-detail.component';

describe('AgendaEmpleado Management Detail Component', () => {
  let comp: AgendaEmpleadoDetailComponent;
  let fixture: ComponentFixture<AgendaEmpleadoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AgendaEmpleadoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ agendaEmpleado: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AgendaEmpleadoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AgendaEmpleadoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load agendaEmpleado on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.agendaEmpleado).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
