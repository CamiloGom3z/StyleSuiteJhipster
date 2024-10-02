package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.EstablecimientoRepository;
import com.pruebaproyecto.app.service.EstablecimientoService;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Establecimiento}.
 */
@RestController
@RequestMapping("/api")
public class EstablecimientoResource {

    private final Logger log = LoggerFactory.getLogger(EstablecimientoResource.class);

    private static final String ENTITY_NAME = "establecimiento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstablecimientoService establecimientoService;

    private final EstablecimientoRepository establecimientoRepository;

    public EstablecimientoResource(EstablecimientoService establecimientoService, EstablecimientoRepository establecimientoRepository) {
        this.establecimientoService = establecimientoService;
        this.establecimientoRepository = establecimientoRepository;
    }

    /**
     * {@code POST  /establecimientos} : Create a new establecimiento.
     *
     * @param establecimientoDTO the establecimientoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new establecimientoDTO, or with status {@code 400 (Bad Request)} if the establecimiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/establecimientos")
    public ResponseEntity<EstablecimientoDTO> createEstablecimiento(@RequestBody EstablecimientoDTO establecimientoDTO)
        throws URISyntaxException {
        log.debug("REST request to save Establecimiento : {}", establecimientoDTO);
        if (establecimientoDTO.getId() != null) {
            throw new BadRequestAlertException("A new establecimiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstablecimientoDTO result = establecimientoService.save(establecimientoDTO);
        return ResponseEntity
            .created(new URI("/api/establecimientos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /establecimientos/:id} : Updates an existing establecimiento.
     *
     * @param id the id of the establecimientoDTO to save.
     * @param establecimientoDTO the establecimientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated establecimientoDTO,
     * or with status {@code 400 (Bad Request)} if the establecimientoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the establecimientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/establecimientos/{id}")
    public ResponseEntity<EstablecimientoDTO> updateEstablecimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstablecimientoDTO establecimientoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Establecimiento : {}, {}", id, establecimientoDTO);
        if (establecimientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, establecimientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!establecimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstablecimientoDTO result = establecimientoService.update(establecimientoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, establecimientoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /establecimientos/:id} : Partial updates given fields of an existing establecimiento, field will ignore if it is null
     *
     * @param id the id of the establecimientoDTO to save.
     * @param establecimientoDTO the establecimientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated establecimientoDTO,
     * or with status {@code 400 (Bad Request)} if the establecimientoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the establecimientoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the establecimientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/establecimientos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstablecimientoDTO> partialUpdateEstablecimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstablecimientoDTO establecimientoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Establecimiento partially : {}, {}", id, establecimientoDTO);
        if (establecimientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, establecimientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!establecimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstablecimientoDTO> result = establecimientoService.partialUpdate(establecimientoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, establecimientoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /establecimientos} : get all the establecimientos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of establecimientos in body.
     */
    @GetMapping("/establecimientos")
    public List<EstablecimientoDTO> getAllEstablecimientos() {
        log.debug("REST request to get all Establecimientos");
        return establecimientoService.findAll();
    }

    /**
     * {@code GET  /establecimientos/:id} : get the "id" establecimiento.
     *
     * @param id the id of the establecimientoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the establecimientoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/establecimientos/{id}")
    public ResponseEntity<EstablecimientoDTO> getEstablecimiento(@PathVariable Long id) {
        log.debug("REST request to get Establecimiento : {}", id);
        Optional<EstablecimientoDTO> establecimientoDTO = establecimientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(establecimientoDTO);
    }

    /**
     * {@code DELETE  /establecimientos/:id} : delete the "id" establecimiento.
     *
     * @param id the id of the establecimientoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/establecimientos/{id}")
    public ResponseEntity<Void> deleteEstablecimiento(@PathVariable Long id) {
        log.debug("REST request to delete Establecimiento : {}", id);
        establecimientoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
