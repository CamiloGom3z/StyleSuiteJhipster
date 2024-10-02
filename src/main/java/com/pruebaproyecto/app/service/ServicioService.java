package com.pruebaproyecto.app.service;

import com.pruebaproyecto.app.service.dto.ServicioDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.pruebaproyecto.app.domain.Servicio}.
 */
public interface ServicioService {
    /**
     * Save a servicio.
     *
     * @param servicioDTO the entity to save.
     * @return the persisted entity.
     */
    ServicioDTO save(ServicioDTO servicioDTO);

    /**
     * Updates a servicio.
     *
     * @param servicioDTO the entity to update.
     * @return the persisted entity.
     */
    ServicioDTO update(ServicioDTO servicioDTO);

    /**
     * Partially updates a servicio.
     *
     * @param servicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServicioDTO> partialUpdate(ServicioDTO servicioDTO);

    /**
     * Get all the servicios.
     *
     * @return the list of entities.
     */
    List<ServicioDTO> findAll();

    /**
     * Get all the servicios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServicioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" servicio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServicioDTO> findOne(Long id);

    /**
     * Delete the "id" servicio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
