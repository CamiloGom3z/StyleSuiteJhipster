package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Establecimiento}.
 */
public interface EstablecimientoService {
    /**
     * Save a establecimiento.
     *
     * @param establecimientoDTO the entity to save.
     * @return the persisted entity.
     */
    EstablecimientoDTO save(EstablecimientoDTO establecimientoDTO);

    /**
     * Updates a establecimiento.
     *
     * @param establecimientoDTO the entity to update.
     * @return the persisted entity.
     */
    EstablecimientoDTO update(EstablecimientoDTO establecimientoDTO);

    /**
     * Partially updates a establecimiento.
     *
     * @param establecimientoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EstablecimientoDTO> partialUpdate(EstablecimientoDTO establecimientoDTO);

    /**
     * Get all the establecimientos.
     *
     * @return the list of entities.
     */
    List<EstablecimientoDTO> findAll();

    /**
     * Get the "id" establecimiento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstablecimientoDTO> findOne(Long id);

    /**
     * Delete the "id" establecimiento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
