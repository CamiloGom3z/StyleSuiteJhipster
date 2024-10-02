package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.ReseniaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Resenia}.
 */
public interface ReseniaService {
    /**
     * Save a resenia.
     *
     * @param reseniaDTO the entity to save.
     * @return the persisted entity.
     */
    ReseniaDTO save(ReseniaDTO reseniaDTO);

    /**
     * Updates a resenia.
     *
     * @param reseniaDTO the entity to update.
     * @return the persisted entity.
     */
    ReseniaDTO update(ReseniaDTO reseniaDTO);

    /**
     * Partially updates a resenia.
     *
     * @param reseniaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReseniaDTO> partialUpdate(ReseniaDTO reseniaDTO);

    /**
     * Get all the resenias.
     *
     * @return the list of entities.
     */
    List<ReseniaDTO> findAll();

    /**
     * Get the "id" resenia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReseniaDTO> findOne(Long id);

    /**
     * Delete the "id" resenia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
