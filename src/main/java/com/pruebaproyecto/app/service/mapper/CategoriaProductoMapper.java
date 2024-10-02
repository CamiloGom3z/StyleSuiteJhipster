package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.CategoriaProducto;
import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.service.dto.CategoriaProductoDTO;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaProducto} and its DTO {@link CategoriaProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaProductoMapper extends EntityMapper<CategoriaProductoDTO, CategoriaProducto> {
    @Mapping(target = "establecimiento", source = "establecimiento", qualifiedByName = "establecimientoId")
    CategoriaProductoDTO toDto(CategoriaProducto s);

    @Named("establecimientoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstablecimientoDTO toDtoEstablecimientoId(Establecimiento establecimiento);
}
