package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Cita;
import com.pruebaproyecto.app.domain.Pago;
import com.pruebaproyecto.app.service.dto.CitaDTO;
import com.pruebaproyecto.app.service.dto.PagoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pago} and its DTO {@link PagoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PagoMapper extends EntityMapper<PagoDTO, Pago> {
    @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
    PagoDTO toDto(Pago s);

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);
}
