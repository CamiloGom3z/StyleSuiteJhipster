package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.PersonaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Persona}.
 */
public interface PersonaService {
    /**
     * Save a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    PersonaDTO save(PersonaDTO personaDTO);

    /**
     * Updates a persona.
     *
     * @param personaDTO the entity to update.
     * @return the persisted entity.
     */
    PersonaDTO update(PersonaDTO personaDTO);

    /**
     * Partially updates a persona.
     *
     * @param personaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO);

    /**
     * Get all the personas.
     *
     * @return the list of entities.
     */
    List<PersonaDTO> findAll();

    /**
     * Get the "id" persona.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonaDTO> findOne(Long id);

    /**
     * Delete the "id" persona.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
