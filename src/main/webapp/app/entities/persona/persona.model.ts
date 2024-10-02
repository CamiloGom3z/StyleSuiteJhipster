import dayjs from 'dayjs/esm';
import { ICita } from 'app/entities/cita/cita.model';

export interface IPersona {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  correoElectronico?: string | null;
  telefono?: string | null;
  citas?: ICita[] | null;
}

export class Persona implements IPersona {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public correoElectronico?: string | null,
    public telefono?: string | null,
    public citas?: ICita[] | null
  ) {}
}

export function getPersonaIdentifier(persona: IPersona): number | undefined {
  return persona.id;
}
