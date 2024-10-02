import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CitaService } from '../service/cita.service';

import { CitaComponent } from './cita.component';

describe('Cita Management Component', () => {
  let comp: CitaComponent;
  let fixture: ComponentFixture<CitaComponent>;
  let service: CitaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CitaComponent],
    })
      .overrideTemplate(CitaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CitaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CitaService);

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
    expect(comp.citas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
