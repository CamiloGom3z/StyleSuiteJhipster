package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.TipoServcio;
import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoServcio} and its DTO {@link TipoServcioDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoServcioMapper extends EntityMapper<TipoServcioDTO, TipoServcio> {}
