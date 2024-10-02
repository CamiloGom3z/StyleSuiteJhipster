package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Empleado;
import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.domain.Persona;
import com.pruebaproyecto.app.service.dto.EmpleadoDTO;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import com.pruebaproyecto.app.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaId")
    @Mapping(target = "establecimiento", source = "establecimiento", qualifiedByName = "establecimientoId")
    EmpleadoDTO toDto(Empleado s);

    @Named("personaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonaDTO toDtoPersonaId(Persona persona);

    @Named("establecimientoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstablecimientoDTO toDtoEstablecimientoId(Establecimiento establecimiento);
}
