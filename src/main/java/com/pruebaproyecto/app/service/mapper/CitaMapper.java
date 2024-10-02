package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.AgendaEmpleado;
import com.pruebaproyecto.app.domain.Cita;
import com.pruebaproyecto.app.domain.Persona;
import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import com.pruebaproyecto.app.service.dto.CitaDTO;
import com.pruebaproyecto.app.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "agendaEmpleado", source = "agendaEmpleado", qualifiedByName = "agendaEmpleadoId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "personaId")
    CitaDTO toDto(Cita s);

    @Named("agendaEmpleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgendaEmpleadoDTO toDtoAgendaEmpleadoId(AgendaEmpleado agendaEmpleado);

    @Named("personaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonaDTO toDtoPersonaId(Persona persona);
}
