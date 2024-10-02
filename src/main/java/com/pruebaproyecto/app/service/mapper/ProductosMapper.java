package com.pruebaproyecto.app.service.mapper;

import com.pruebaproyecto.app.domain.CategoriaProducto;
import com.pruebaproyecto.app.domain.Productos;
import com.pruebaproyecto.app.service.dto.CategoriaProductoDTO;
import com.pruebaproyecto.app.service.dto.ProductosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Productos} and its DTO {@link ProductosDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductosMapper extends EntityMapper<ProductosDTO, Productos> {
    @Mapping(target = "categoriaProducto", source = "categoriaProducto", qualifiedByName = "categoriaProductoNombre")
    ProductosDTO toDto(Productos s);

    @Named("categoriaProductoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CategoriaProductoDTO toDtoCategoriaProductoNombre(CategoriaProducto categoriaProducto);
}
