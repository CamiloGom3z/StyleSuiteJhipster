import dayjs from 'dayjs/esm';
import { ICita } from 'app/entities/cita/cita.model';
import { MetodoPagoEnum } from 'app/entities/enumerations/metodo-pago-enum.model';

export interface IPago {
  id?: number;
  monto?: number | null;
  fechaPago?: dayjs.Dayjs | null;
  metodoPago?: MetodoPagoEnum | null;
  estado?: string | null;
  cita?: ICita | null;
}

export class Pago implements IPago {
  constructor(
    public id?: number,
    public monto?: number | null,
    public fechaPago?: dayjs.Dayjs | null,
    public metodoPago?: MetodoPagoEnum | null,
    public estado?: string | null,
    public cita?: ICita | null
  ) {}
}

export function getPagoIdentifier(pago: IPago): number | undefined {
  return pago.id;
}
