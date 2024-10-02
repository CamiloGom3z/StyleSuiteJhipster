package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.AgendaEmpleado;
import com.pruebaproyecto.app.domain.Empleado;
import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import com.pruebaproyecto.app.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgendaEmpleado} and its DTO {@link AgendaEmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgendaEmpleadoMapper extends EntityMapper<AgendaEmpleadoDTO, AgendaEmpleado> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    AgendaEmpleadoDTO toDto(AgendaEmpleado s);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);
}
