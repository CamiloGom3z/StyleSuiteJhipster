import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoServcioService } from '../service/tipo-servcio.service';

import { TipoServcioComponent } from './tipo-servcio.component';

describe('TipoServcio Management Component', () => {
  let comp: TipoServcioComponent;
  let fixture: ComponentFixture<TipoServcioComponent>;
  let service: TipoServcioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoServcioComponent],
    })
      .overrideTemplate(TipoServcioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoServcioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoServcioService);

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
    expect(comp.tipoServcios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
