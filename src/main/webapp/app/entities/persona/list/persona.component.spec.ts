import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonaService } from '../service/persona.service';

import { PersonaComponent } from './persona.component';

describe('Persona Management Component', () => {
  let comp: PersonaComponent;
  let fixture: ComponentFixture<PersonaComponent>;
  let service: PersonaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PersonaComponent],
    })
      .overrideTemplate(PersonaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PersonaService);

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
    expect(comp.personas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
