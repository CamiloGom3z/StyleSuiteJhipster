package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.AgendaEmpleado}.
 */
public interface AgendaEmpleadoService {
    /**
     * Save a agendaEmpleado.
     *
     * @param agendaEmpleadoDTO the entity to save.
     * @return the persisted entity.
     */
    AgendaEmpleadoDTO save(AgendaEmpleadoDTO agendaEmpleadoDTO);

    /**
     * Updates a agendaEmpleado.
     *
     * @param agendaEmpleadoDTO the entity to update.
     * @return the persisted entity.
     */
    AgendaEmpleadoDTO update(AgendaEmpleadoDTO agendaEmpleadoDTO);

    /**
     * Partially updates a agendaEmpleado.
     *
     * @param agendaEmpleadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgendaEmpleadoDTO> partialUpdate(AgendaEmpleadoDTO agendaEmpleadoDTO);

    /**
     * Get all the agendaEmpleados.
     *
     * @return the list of entities.
     */
    List<AgendaEmpleadoDTO> findAll();
    /**
     * Get all the AgendaEmpleadoDTO where Cita is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AgendaEmpleadoDTO> findAllWhereCitaIsNull();

    /**
     * Get the "id" agendaEmpleado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgendaEmpleadoDTO> findOne(Long id);

    /**
     * Delete the "id" agendaEmpleado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
