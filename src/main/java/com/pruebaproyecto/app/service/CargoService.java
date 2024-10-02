package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.CargoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Cargo}.
 */
public interface CargoService {
    /**
     * Save a cargo.
     *
     * @param cargoDTO the entity to save.
     * @return the persisted entity.
     */
    CargoDTO save(CargoDTO cargoDTO);

    /**
     * Updates a cargo.
     *
     * @param cargoDTO the entity to update.
     * @return the persisted entity.
     */
    CargoDTO update(CargoDTO cargoDTO);

    /**
     * Partially updates a cargo.
     *
     * @param cargoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CargoDTO> partialUpdate(CargoDTO cargoDTO);

    /**
     * Get all the cargos.
     *
     * @return the list of entities.
     */
    List<CargoDTO> findAll();

    /**
     * Get the "id" cargo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CargoDTO> findOne(Long id);

    /**
     * Delete the "id" cargo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
