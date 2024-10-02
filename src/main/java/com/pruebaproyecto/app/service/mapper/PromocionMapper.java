package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.Promocion;
import com.pruebaproyecto.app.domain.Servicio;
import com.pruebaproyecto.app.service.dto.PromocionDTO;
import com.pruebaproyecto.app.service.dto.ServicioDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Promocion} and its DTO {@link PromocionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromocionMapper extends EntityMapper<PromocionDTO, Promocion> {
    @Mapping(target = "servicios", source = "servicios", qualifiedByName = "servicioIdSet")
    PromocionDTO toDto(Promocion s);

    @Mapping(target = "removeServicios", ignore = true)
    Promocion toEntity(PromocionDTO promocionDTO);

    @Named("servicioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDTO toDtoServicioId(Servicio servicio);

    @Named("servicioIdSet")
    default Set<ServicioDTO> toDtoServicioIdSet(Set<Servicio> servicio) {
        return servicio.stream().map(this::toDtoServicioId).collect(Collectors.toSet());
    }
}
