import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface ICargo {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  empleado?: IEmpleado | null;
}

export class Cargo implements ICargo {
  constructor(public id?: number, public nombre?: string | null, public descripcion?: string | null, public empleado?: IEmpleado | null) {}
}

export function getCargoIdentifier(cargo: ICargo): number | undefined {
  return cargo.id;
}
