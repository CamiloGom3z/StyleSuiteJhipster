import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PagoService } from '../service/pago.service';

import { PagoComponent } from './pago.component';

describe('Pago Management Component', () => {
  let comp: PagoComponent;
  let fixture: ComponentFixture<PagoComponent>;
  let service: PagoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PagoComponent],
    })
      .overrideTemplate(PagoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PagoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PagoService);

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
    expect(comp.pagos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
