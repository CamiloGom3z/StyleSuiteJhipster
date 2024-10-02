package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.CategoriaImagen;
import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaImagen} and its DTO {@link CategoriaImagenDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaImagenMapper extends EntityMapper<CategoriaImagenDTO, CategoriaImagen> {
    @Mapping(target = "establecimiento", source = "establecimiento", qualifiedByName = "establecimientoId")
    CategoriaImagenDTO toDto(CategoriaImagen s);

    @Named("establecimientoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstablecimientoDTO toDtoEstablecimientoId(Establecimiento establecimiento);
}
