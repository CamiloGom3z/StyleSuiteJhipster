import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ImagenService } from '../service/imagen.service';

import { ImagenComponent } from './imagen.component';

describe('Imagen Management Component', () => {
  let comp: ImagenComponent;
  let fixture: ComponentFixture<ImagenComponent>;
  let service: ImagenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ImagenComponent],
    })
      .overrideTemplate(ImagenComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagenComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImagenService);

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
    expect(comp.imagens?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
