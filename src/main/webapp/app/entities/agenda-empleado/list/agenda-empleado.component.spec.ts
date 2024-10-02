import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AgendaEmpleadoService } from '../service/agenda-empleado.service';

import { AgendaEmpleadoComponent } from './agenda-empleado.component';

describe('AgendaEmpleado Management Component', () => {
  let comp: AgendaEmpleadoComponent;
  let fixture: ComponentFixture<AgendaEmpleadoComponent>;
  let service: AgendaEmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AgendaEmpleadoComponent],
    })
      .overrideTemplate(AgendaEmpleadoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendaEmpleadoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AgendaEmpleadoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.agendaEmpleados?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
