package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Establecimiento} and its DTO {@link EstablecimientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstablecimientoMapper extends EntityMapper<EstablecimientoDTO, Establecimiento> {}
