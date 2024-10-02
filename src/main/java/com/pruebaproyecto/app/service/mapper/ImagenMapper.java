package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.CategoriaImagen;
import com.pruebaproyecto.app.domain.Imagen;
import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
import com.pruebaproyecto.app.service.dto.ImagenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Imagen} and its DTO {@link ImagenDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagenMapper extends EntityMapper<ImagenDTO, Imagen> {
    @Mapping(target = "categoriaImagen", source = "categoriaImagen", qualifiedByName = "categoriaImagenNombre")
    ImagenDTO toDto(Imagen s);

    @Named("categoriaImagenNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CategoriaImagenDTO toDtoCategoriaImagenNombre(CategoriaImagen categoriaImagen);
}
