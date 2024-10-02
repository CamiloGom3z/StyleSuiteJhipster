import { IAgenda } from 'app/entities/agenda/agenda.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';
import { ICategoriaImagen } from 'app/entities/categoria-imagen/categoria-imagen.model';

export interface IEstablecimiento {
  id?: number;
  nombre?: string | null;
  nit?: number | null;
  direccion?: string | null;
  telefono?: string | null;
  correoElectronico?: string | null;
  agendas?: IAgenda[] | null;
  empleados?: IEmpleado[] | null;
  categoriasProductos?: ICategoriaProducto[] | null;
  categoriasImagens?: ICategoriaImagen[] | null;
}

export class Establecimiento implements IEstablecimiento {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public nit?: number | null,
    public direccion?: string | null,
    public telefono?: string | null,
    public correoElectronico?: string | null,
    public agendas?: IAgenda[] | null,
    public empleados?: IEmpleado[] | null,
    public categoriasProductos?: ICategoriaProducto[] | null,
    public categoriasImagens?: ICategoriaImagen[] | null
  ) {}
}

export function getEstablecimientoIdentifier(establecimiento: IEstablecimiento): number | undefined {
  return establecimiento.id;
}
