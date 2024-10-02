import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EstablecimientoService } from '../service/establecimiento.service';

import { EstablecimientoComponent } from './establecimiento.component';

describe('Establecimiento Management Component', () => {
  let comp: EstablecimientoComponent;
  let fixture: ComponentFixture<EstablecimientoComponent>;
  let service: EstablecimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EstablecimientoComponent],
    })
      .overrideTemplate(EstablecimientoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstablecimientoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EstablecimientoService);

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
    expect(comp.establecimientos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
