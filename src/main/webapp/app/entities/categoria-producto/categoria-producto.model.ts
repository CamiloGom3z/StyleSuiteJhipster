import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';

export interface ICategoriaProducto {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  establecimiento?: IEstablecimiento | null;
}

export class CategoriaProducto implements ICategoriaProducto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public establecimiento?: IEstablecimiento | null
  ) {}
}

export function getCategoriaProductoIdentifier(categoriaProducto: ICategoriaProducto): number | undefined {
  return categoriaProducto.id;
}
