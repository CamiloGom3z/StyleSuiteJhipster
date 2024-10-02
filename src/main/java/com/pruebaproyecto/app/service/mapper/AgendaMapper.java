package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Agenda;
import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.service.dto.AgendaDTO;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agenda} and its DTO {@link AgendaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgendaMapper extends EntityMapper<AgendaDTO, Agenda> {
    @Mapping(target = "establecimiento", source = "establecimiento", qualifiedByName = "establecimientoId")
    AgendaDTO toDto(Agenda s);

    @Named("establecimientoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstablecimientoDTO toDtoEstablecimientoId(Establecimiento establecimiento);
}
