import { IPersona } from 'app/entities/persona/persona.model';
import { ICargo } from 'app/entities/cargo/cargo.model';
import { IAgendaEmpleado } from 'app/entities/agenda-empleado/agenda-empleado.model';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';

export interface IEmpleado {
  id?: number;
  cargoEmpleado?: string | null;
  salario?: number | null;
  persona?: IPersona | null;
  cargos?: ICargo[] | null;
  agendaEmpleados?: IAgendaEmpleado[] | null;
  establecimiento?: IEstablecimiento | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public cargoEmpleado?: string | null,
    public salario?: number | null,
    public persona?: IPersona | null,
    public cargos?: ICargo[] | null,
    public agendaEmpleados?: IAgendaEmpleado[] | null,
    public establecimiento?: IEstablecimiento | null
  ) {}
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
