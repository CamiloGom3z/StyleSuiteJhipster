import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AgendaService } from '../service/agenda.service';

import { AgendaComponent } from './agenda.component';

describe('Agenda Management Component', () => {
  let comp: AgendaComponent;
  let fixture: ComponentFixture<AgendaComponent>;
  let service: AgendaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AgendaComponent],
    })
      .overrideTemplate(AgendaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgendaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AgendaService);

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
    expect(comp.agenda?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
