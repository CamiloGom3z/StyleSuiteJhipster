package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.EmpleadoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Empleado}.
 */
public interface EmpleadoService {
    /**
     * Save a empleado.
     *
     * @param empleadoDTO the entity to save.
     * @return the persisted entity.
     */
    EmpleadoDTO save(EmpleadoDTO empleadoDTO);

    /**
     * Updates a empleado.
     *
     * @param empleadoDTO the entity to update.
     * @return the persisted entity.
     */
    EmpleadoDTO update(EmpleadoDTO empleadoDTO);

    /**
     * Partially updates a empleado.
     *
     * @param empleadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmpleadoDTO> partialUpdate(EmpleadoDTO empleadoDTO);

    /**
     * Get all the empleados.
     *
     * @return the list of entities.
     */
    List<EmpleadoDTO> findAll();

    /**
     * Get the "id" empleado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmpleadoDTO> findOne(Long id);

    /**
     * Delete the "id" empleado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
