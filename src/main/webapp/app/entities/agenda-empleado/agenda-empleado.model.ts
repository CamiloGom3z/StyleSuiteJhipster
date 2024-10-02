import dayjs from 'dayjs/esm';
import { ICita } from 'app/entities/cita/cita.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface IAgendaEmpleado {
  id?: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  disponible?: boolean | null;
  cita?: ICita | null;
  empleado?: IEmpleado | null;
}

export class AgendaEmpleado implements IAgendaEmpleado {
  constructor(
    public id?: number,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public disponible?: boolean | null,
    public cita?: ICita | null,
    public empleado?: IEmpleado | null
  ) {
    this.disponible = this.disponible ?? false;
  }
}

export function getAgendaEmpleadoIdentifier(agendaEmpleado: IAgendaEmpleado): number | undefined {
  return agendaEmpleado.id;
}
