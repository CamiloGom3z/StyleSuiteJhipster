import dayjs from 'dayjs/esm';
import { IAgendaEmpleado } from 'app/entities/agenda-empleado/agenda-empleado.model';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IPago } from 'app/entities/pago/pago.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { EstadoCitaEnum } from 'app/entities/enumerations/estado-cita-enum.model';

export interface ICita {
  id?: number;
  fechaCita?: dayjs.Dayjs | null;
  fechaFinCita?: dayjs.Dayjs | null;
  estado?: EstadoCitaEnum | null;
  notas?: string | null;
  agendaEmpleado?: IAgendaEmpleado | null;
  servicios?: IServicio[] | null;
  pagos?: IPago[] | null;
  cliente?: IPersona | null;
}

export class Cita implements ICita {
  constructor(
    public id?: number,
    public fechaCita?: dayjs.Dayjs | null,
    public fechaFinCita?: dayjs.Dayjs | null,
    public estado?: EstadoCitaEnum | null,
    public notas?: string | null,
    public agendaEmpleado?: IAgendaEmpleado | null,
    public servicios?: IServicio[] | null,
    public pagos?: IPago[] | null,
    public cliente?: IPersona | null
  ) {}
}

export function getCitaIdentifier(cita: ICita): number | undefined {
  return cita.id;
}
