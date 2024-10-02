import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmpleado, Empleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';
import { EstablecimientoService } from 'app/entities/establecimiento/service/establecimiento.service';
import { ICargo } from 'app/entities/cargo/cargo.model';
import { CargoService } from 'app/entities/cargo/service/cargo.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;

  personasCollection: IPersona[] = [];
  establecimientosSharedCollection: IEstablecimiento[] = [];
  cargosSharedColletion: ICargo[] = [];
 

  editForm = this.fb.group({
    id: [],
    cargoEmpleado: [],
    salario: [],
    persona: [],
    establecimiento: [],
  });

  constructor(
    protected empleadoService: EmpleadoService,
    protected personaService: PersonaService,
    protected cargoService: CargoService,
    protected establecimientoService: EstablecimientoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.updateForm(empleado);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.createFromForm();
    if (empleado.id !== undefined) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  trackPersonaById(_index: number, item: IPersona): number {
    return item.id!;
  }

  trackEstablecimientoById(_index: number, item: IEstablecimiento): number {
    return item.id!;
  }
  trackCargoById(_index: number, item: ICargo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(empleado: IEmpleado): void {
    this.editForm.patchValue({
      id: empleado.id,
      cargoEmpleado: empleado.cargoEmpleado,
      salario: empleado.salario,
      persona: empleado.persona,
      establecimiento: empleado.establecimiento,
    });

    this.personasCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasCollection, empleado.persona);
    this.establecimientosSharedCollection = this.establecimientoService.addEstablecimientoToCollectionIfMissing(
      this.establecimientosSharedCollection,
      empleado.establecimiento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personaService
      .query({ filter: 'empleado-is-null' })
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('persona')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasCollection = personas));

    this.establecimientoService
      .query()
      .pipe(map((res: HttpResponse<IEstablecimiento[]>) => res.body ?? []))
      .pipe(
        map((establecimientos: IEstablecimiento[]) =>
          this.establecimientoService.addEstablecimientoToCollectionIfMissing(establecimientos, this.editForm.get('establecimiento')!.value)
        )
      )
      .subscribe((establecimientos: IEstablecimiento[]) => (this.establecimientosSharedCollection = establecimientos));
  }

  protected createFromForm(): IEmpleado {
    return {
      ...new Empleado(),
      id: this.editForm.get(['id'])!.value,
      cargoEmpleado: this.editForm.get(['cargoEmpleado'])!.value,
      salario: this.editForm.get(['salario'])!.value,
      persona: this.editForm.get(['persona'])!.value,
      establecimiento: this.editForm.get(['establecimiento'])!.value,
    };
  }
}
