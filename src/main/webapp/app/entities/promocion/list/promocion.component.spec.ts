import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PromocionService } from '../service/promocion.service';

import { PromocionComponent } from './promocion.component';

describe('Promocion Management Component', () => {
  let comp: PromocionComponent;
  let fixture: ComponentFixture<PromocionComponent>;
  let service: PromocionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PromocionComponent],
    })
      .overrideTemplate(PromocionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromocionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PromocionService);

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
    expect(comp.promocions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
