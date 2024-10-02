package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Cargo;
import com.pruebaproyecto.app.domain.Empleado;
import com.pruebaproyecto.app.service.dto.CargoDTO;
import com.pruebaproyecto.app.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cargo} and its DTO {@link CargoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CargoMapper extends EntityMapper<CargoDTO, Cargo> {
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    CargoDTO toDto(Cargo s);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);
}
