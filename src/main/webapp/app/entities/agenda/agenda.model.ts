import dayjs from 'dayjs/esm';
import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';

export interface IAgenda {
  id?: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  disponible?: boolean | null;
  establecimiento?: IEstablecimiento | null;
}

export class Agenda implements IAgenda {
  constructor(
    public id?: number,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public disponible?: boolean | null,
    public establecimiento?: IEstablecimiento | null
  ) {
    this.disponible = this.disponible ?? false;
  }
}

export function getAgendaIdentifier(agenda: IAgenda): number | undefined {
  return agenda.id;
}
