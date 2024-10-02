import dayjs from 'dayjs/esm';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IPromocion {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  porcentajeDescuento?: number | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  tipoPromocion?: string | null;
  servicios?: IServicio[] | null;
}

export class Promocion implements IPromocion {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public porcentajeDescuento?: number | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public tipoPromocion?: string | null,
    public servicios?: IServicio[] | null
  ) {}
}

export function getPromocionIdentifier(promocion: IPromocion): number | undefined {
  return promocion.id;
}
