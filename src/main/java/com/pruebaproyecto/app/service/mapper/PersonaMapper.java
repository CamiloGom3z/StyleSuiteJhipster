package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Persona;
import com.pruebaproyecto.app.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {}
