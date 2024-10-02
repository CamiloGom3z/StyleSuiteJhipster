import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ReseniaService } from '../service/resenia.service';

import { ReseniaComponent } from './resenia.component';

describe('Resenia Management Component', () => {
  let comp: ReseniaComponent;
  let fixture: ComponentFixture<ReseniaComponent>;
  let service: ReseniaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReseniaComponent],
    })
      .overrideTemplate(ReseniaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReseniaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReseniaService);

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
    expect(comp.resenias?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
