package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.ServicioRepository;
import com.pruebaproyecto.app.service.ServicioService;
import com.pruebaproyecto.app.service.dto.ServicioDTO;
import com.pruebaproyecto.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Servicio}.
 */
@RestController
@RequestMapping("/api")
public class ServicioResource {

    private final Logger log = LoggerFactory.getLogger(ServicioResource.class);

    private static final String ENTITY_NAME = "servicio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicioService servicioService;

    private final ServicioRepository servicioRepository;

    public ServicioResource(ServicioService servicioService, ServicioRepository servicioRepository) {
        this.servicioService = servicioService;
        this.servicioRepository = servicioRepository;
    }

    /**
     * {@code POST  /servicios} : Create a new servicio.
     *
     * @param servicioDTO the servicioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicioDTO, or with status {@code 400 (Bad Request)} if the servicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/servicios")
    public ResponseEntity<ServicioDTO> createServicio(@RequestBody ServicioDTO servicioDTO) throws URISyntaxException {
        log.debug("REST request to save Servicio : {}", servicioDTO);
        if (servicioDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServicioDTO result = servicioService.save(servicioDTO);
        return ResponseEntity
            .created(new URI("/api/servicios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /servicios/:id} : Updates an existing servicio.
     *
     * @param id the id of the servicioDTO to save.
     * @param servicioDTO the servicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> updateServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServicioDTO servicioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Servicio : {}, {}", id, servicioDTO);
        if (servicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServicioDTO result = servicioService.update(servicioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /servicios/:id} : Partial updates given fields of an existing servicio, field will ignore if it is null
     *
     * @param id the id of the servicioDTO to save.
     * @param servicioDTO the servicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/servicios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicioDTO> partialUpdateServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServicioDTO servicioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Servicio partially : {}, {}", id, servicioDTO);
        if (servicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicioDTO> result = servicioService.partialUpdate(servicioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /servicios} : get all the servicios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicios in body.
     */
    @GetMapping("/servicios")
    public List<ServicioDTO> getAllServicios(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Servicios");
        return servicioService.findAll();
    }

    /**
     * {@code GET  /servicios/:id} : get the "id" servicio.
     *
     * @param id the id of the servicioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> getServicio(@PathVariable Long id) {
        log.debug("REST request to get Servicio : {}", id);
        Optional<ServicioDTO> servicioDTO = servicioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicioDTO);
    }

    /**
     * {@code DELETE  /servicios/:id} : delete the "id" servicio.
     *
     * @param id the id of the servicioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        log.debug("REST request to delete Servicio : {}", id);
        servicioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
