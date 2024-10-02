import dayjs from 'dayjs/esm';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IResenia {
  id?: number;
  calificacion?: number | null;
  comentario?: string | null;
  fecha?: dayjs.Dayjs | null;
  servicio?: IServicio | null;
}

export class Resenia implements IResenia {
  constructor(
    public id?: number,
    public calificacion?: number | null,
    public comentario?: string | null,
    public fecha?: dayjs.Dayjs | null,
    public servicio?: IServicio | null
  ) {}
}

export function getReseniaIdentifier(resenia: IResenia): number | undefined {
  return resenia.id;
}
