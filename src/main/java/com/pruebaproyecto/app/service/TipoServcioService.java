package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.TipoServcio}.
 */
public interface TipoServcioService {
    /**
     * Save a tipoServcio.
     *
     * @param tipoServcioDTO the entity to save.
     * @return the persisted entity.
     */
    TipoServcioDTO save(TipoServcioDTO tipoServcioDTO);

    /**
     * Updates a tipoServcio.
     *
     * @param tipoServcioDTO the entity to update.
     * @return the persisted entity.
     */
    TipoServcioDTO update(TipoServcioDTO tipoServcioDTO);

    /**
     * Partially updates a tipoServcio.
     *
     * @param tipoServcioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TipoServcioDTO> partialUpdate(TipoServcioDTO tipoServcioDTO);

    /**
     * Get all the tipoServcios.
     *
     * @return the list of entities.
     */
    List<TipoServcioDTO> findAll();

    /**
     * Get the "id" tipoServcio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TipoServcioDTO> findOne(Long id);

    /**
     * Delete the "id" tipoServcio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
