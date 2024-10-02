package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.CategoriaProductoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.CategoriaProducto}.
 */
public interface CategoriaProductoService {
    /**
     * Save a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaProductoDTO save(CategoriaProductoDTO categoriaProductoDTO);

    /**
     * Updates a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to update.
     * @return the persisted entity.
     */
    CategoriaProductoDTO update(CategoriaProductoDTO categoriaProductoDTO);

    /**
     * Partially updates a categoriaProducto.
     *
     * @param categoriaProductoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaProductoDTO> partialUpdate(CategoriaProductoDTO categoriaProductoDTO);

    /**
     * Get all the categoriaProductos.
     *
     * @return the list of entities.
     */
    List<CategoriaProductoDTO> findAll();

    /**
     * Get the "id" categoriaProducto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaProductoDTO> findOne(Long id);

    /**
     * Delete the "id" categoriaProducto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
