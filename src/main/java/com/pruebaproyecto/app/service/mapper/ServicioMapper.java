package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Cita;
import com.pruebaproyecto.app.domain.Servicio;
import com.pruebaproyecto.app.domain.TipoServcio;
import com.pruebaproyecto.app.service.dto.CitaDTO;
import com.pruebaproyecto.app.service.dto.ServicioDTO;
import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
    @Mapping(target = "tipoServicio", source = "tipoServicio", qualifiedByName = "tipoServcioNombre")
    @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
    ServicioDTO toDto(Servicio s);

    @Named("tipoServcioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TipoServcioDTO toDtoTipoServcioNombre(TipoServcio tipoServcio);

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);
}
