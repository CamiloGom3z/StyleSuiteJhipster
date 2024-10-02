import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CategoriaImagenService } from '../service/categoria-imagen.service';

import { CategoriaImagenComponent } from './categoria-imagen.component';

describe('CategoriaImagen Management Component', () => {
  let comp: CategoriaImagenComponent;
  let fixture: ComponentFixture<CategoriaImagenComponent>;
  let service: CategoriaImagenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CategoriaImagenComponent],
    })
      .overrideTemplate(CategoriaImagenComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaImagenComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CategoriaImagenService);

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
    expect(comp.categoriaImagens?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
