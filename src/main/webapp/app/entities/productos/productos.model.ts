import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';

export interface IProductos {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  cantidad?: number | null;
  categoriaProducto?: ICategoriaProducto | null;
}

export class Productos implements IProductos {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public precio?: number | null,
    public cantidad?: number | null,
    public categoriaProducto?: ICategoriaProducto | null
  ) {}
}

export function getProductosIdentifier(productos: IProductos): number | undefined {
  return productos.id;
}
