import { ICategoriaImagen } from 'app/entities/categoria-imagen/categoria-imagen.model';

export interface IImagen {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  urlImagen?: string | null;
  categoriaImagen?: ICategoriaImagen | null;
}

export class Imagen implements IImagen {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public urlImagen?: string | null,
    public categoriaImagen?: ICategoriaImagen | null
  ) {}
}

export function getImagenIdentifier(imagen: IImagen): number | undefined {
  return imagen.id;
}
