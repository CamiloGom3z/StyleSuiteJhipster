package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.AgendaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Agenda}.
 */
public interface AgendaService {
    /**
     * Save a agenda.
     *
     * @param agendaDTO the entity to save.
     * @return the persisted entity.
     */
    AgendaDTO save(AgendaDTO agendaDTO);

    /**
     * Updates a agenda.
     *
     * @param agendaDTO the entity to update.
     * @return the persisted entity.
     */
    AgendaDTO update(AgendaDTO agendaDTO);

    /**
     * Partially updates a agenda.
     *
     * @param agendaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgendaDTO> partialUpdate(AgendaDTO agendaDTO);

    /**
     * Get all the agenda.
     *
     * @return the list of entities.
     */
    List<AgendaDTO> findAll();

    /**
     * Get the "id" agenda.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgendaDTO> findOne(Long id);

    /**
     * Delete the "id" agenda.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
