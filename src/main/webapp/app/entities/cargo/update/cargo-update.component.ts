import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICargo, Cargo } from '../cargo.model';
import { CargoService } from '../service/cargo.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-cargo-update',
  templateUrl: './cargo-update.component.html',
})
export class CargoUpdateComponent implements OnInit {
  isSaving = false;

  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    descripcion: [],
    empleado: [],
  });

  constructor(
    protected cargoService: CargoService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cargo }) => {
      this.updateForm(cargo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cargo = this.createFromForm();
    if (cargo.id !== undefined) {
      this.subscribeToSaveResponse(this.cargoService.update(cargo));
    } else {
      this.subscribeToSaveResponse(this.cargoService.create(cargo));
    }
  }

  trackEmpleadoById(_index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICargo>>): void {
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

  protected updateForm(cargo: ICargo): void {
    this.editForm.patchValue({
      id: cargo.id,
      nombre: cargo.nombre,
      descripcion: cargo.descripcion,
      empleado: cargo.empleado,
    });

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosSharedCollection, cargo.empleado);
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): ICargo {
    return {
      ...new Cargo(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
    };
  }
}
