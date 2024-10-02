import { IEstablecimiento } from 'app/entities/establecimiento/establecimiento.model';

export interface ICategoriaImagen {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  establecimiento?: IEstablecimiento | null;
}

export class CategoriaImagen implements ICategoriaImagen {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public establecimiento?: IEstablecimiento | null
  ) {}
}

export function getCategoriaImagenIdentifier(categoriaImagen: ICategoriaImagen): number | undefined {
  return categoriaImagen.id;
}
