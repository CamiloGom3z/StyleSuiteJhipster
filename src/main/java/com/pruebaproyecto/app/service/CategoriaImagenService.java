package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.CategoriaImagen}.
 */
public interface CategoriaImagenService {
    /**
     * Save a categoriaImagen.
     *
     * @param categoriaImagenDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaImagenDTO save(CategoriaImagenDTO categoriaImagenDTO);

    /**
     * Updates a categoriaImagen.
     *
     * @param categoriaImagenDTO the entity to update.
     * @return the persisted entity.
     */
    CategoriaImagenDTO update(CategoriaImagenDTO categoriaImagenDTO);

    /**
     * Partially updates a categoriaImagen.
     *
     * @param categoriaImagenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaImagenDTO> partialUpdate(CategoriaImagenDTO categoriaImagenDTO);

    /**
     * Get all the categoriaImagens.
     *
     * @return the list of entities.
     */
    List<CategoriaImagenDTO> findAll();

    /**
     * Get the "id" categoriaImagen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaImagenDTO> findOne(Long id);

    /**
     * Delete the "id" categoriaImagen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
