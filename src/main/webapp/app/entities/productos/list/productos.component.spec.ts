import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductosService } from '../service/productos.service';

import { ProductosComponent } from './productos.component';

describe('Productos Management Component', () => {
  let comp: ProductosComponent;
  let fixture: ComponentFixture<ProductosComponent>;
  let service: ProductosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductosComponent],
    })
      .overrideTemplate(ProductosComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductosComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductosService);

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
    expect(comp.productos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
