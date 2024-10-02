package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.ProductosDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Productos}.
 */
public interface ProductosService {
    /**
     * Save a productos.
     *
     * @param productosDTO the entity to save.
     * @return the persisted entity.
     */
    ProductosDTO save(ProductosDTO productosDTO);

    /**
     * Updates a productos.
     *
     * @param productosDTO the entity to update.
     * @return the persisted entity.
     */
    ProductosDTO update(ProductosDTO productosDTO);

    /**
     * Partially updates a productos.
     *
     * @param productosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductosDTO> partialUpdate(ProductosDTO productosDTO);

    /**
     * Get all the productos.
     *
     * @return the list of entities.
     */
    List<ProductosDTO> findAll();

    /**
     * Get all the productos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductosDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductosDTO> findOne(Long id);

    /**
     * Delete the "id" productos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
