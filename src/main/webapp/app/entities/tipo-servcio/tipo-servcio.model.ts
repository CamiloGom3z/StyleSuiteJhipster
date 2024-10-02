export interface ITipoServcio {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  valorTipoServicio?: number | null;
}

export class TipoServcio implements ITipoServcio {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public valorTipoServicio?: number | null
  ) {}
}

export function getTipoServcioIdentifier(tipoServcio: ITipoServcio): number | undefined {
  return tipoServcio.id;
}
