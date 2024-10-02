import dayjs from 'dayjs/esm';
import { IResenia } from 'app/entities/resenia/resenia.model';
import { ITipoServcio } from 'app/entities/tipo-servcio/tipo-servcio.model';
import { ICita } from 'app/entities/cita/cita.model';
import { IPromocion } from 'app/entities/promocion/promocion.model';

export interface IServicio {
  id?: number;
  valorTotalServicio?: number | null;
  descripcion?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  resenias?: IResenia[] | null;
  tipoServicio?: ITipoServcio | null;
  cita?: ICita | null;
  promociones?: IPromocion[] | null;
}

export class Servicio implements IServicio {
  constructor(
    public id?: number,
    public valorTotalServicio?: number | null,
    public descripcion?: string | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public resenias?: IResenia[] | null,
    public tipoServicio?: ITipoServcio | null,
    public cita?: ICita | null,
    public promociones?: IPromocion[] | null
  ) {}
}

export function getServicioIdentifier(servicio: IServicio): number | undefined {
  return servicio.id;
}
