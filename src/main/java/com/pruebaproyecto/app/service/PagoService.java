package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.PagoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Pago}.
 */
public interface PagoService {
    /**
     * Save a pago.
     *
     * @param pagoDTO the entity to save.
     * @return the persisted entity.
     */
    PagoDTO save(PagoDTO pagoDTO);

    /**
     * Updates a pago.
     *
     * @param pagoDTO the entity to update.
     * @return the persisted entity.
     */
    PagoDTO update(PagoDTO pagoDTO);

    /**
     * Partially updates a pago.
     *
     * @param pagoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PagoDTO> partialUpdate(PagoDTO pagoDTO);

    /**
     * Get all the pagos.
     *
     * @return the list of entities.
     */
    List<PagoDTO> findAll();

    /**
     * Get the "id" pago.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PagoDTO> findOne(Long id);

    /**
     * Delete the "id" pago.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
