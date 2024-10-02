package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Resenia;
import com.pruebaproyecto.app.domain.Servicio;
import com.pruebaproyecto.app.service.dto.ReseniaDTO;
import com.pruebaproyecto.app.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resenia} and its DTO {@link ReseniaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReseniaMapper extends EntityMapper<ReseniaDTO, Resenia> {
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioId")
    ReseniaDTO toDto(Resenia s);

    @Named("servicioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDTO toDtoServicioId(Servicio servicio);
}
