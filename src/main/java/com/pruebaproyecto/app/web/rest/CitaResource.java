package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.CitaRepository;
import com.pruebaproyecto.app.service.CitaService;
import com.pruebaproyecto.app.service.dto.CitaDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Cita}.
 */
@RestController
@RequestMapping("/api")
public class CitaResource {

    private final Logger log = LoggerFactory.getLogger(CitaResource.class);

    private static final String ENTITY_NAME = "cita";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitaService citaService;

    private final CitaRepository citaRepository;

    public CitaResource(CitaService citaService, CitaRepository citaRepository) {
        this.citaService = citaService;
        this.citaRepository = citaRepository;
    }

    /**
     * {@code POST  /citas} : Create a new cita.
     *
     * @param citaDTO the citaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citaDTO, or with status {@code 400 (Bad Request)} if the cita has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/citas")
    public ResponseEntity<CitaDTO> createCita(@RequestBody CitaDTO citaDTO) throws URISyntaxException {
        log.debug("REST request to save Cita : {}", citaDTO);
        if (citaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cita cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CitaDTO result = citaService.save(citaDTO);
        return ResponseEntity
            .created(new URI("/api/citas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /citas/:id} : Updates an existing cita.
     *
     * @param id the id of the citaDTO to save.
     * @param citaDTO the citaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaDTO,
     * or with status {@code 400 (Bad Request)} if the citaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/citas/{id}")
    public ResponseEntity<CitaDTO> updateCita(@PathVariable(value = "id", required = false) final Long id, @RequestBody CitaDTO citaDTO)
        throws URISyntaxException {
        log.debug("REST request to update Cita : {}, {}", id, citaDTO);
        if (citaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CitaDTO result = citaService.update(citaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /citas/:id} : Partial updates given fields of an existing cita, field will ignore if it is null
     *
     * @param id the id of the citaDTO to save.
     * @param citaDTO the citaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaDTO,
     * or with status {@code 400 (Bad Request)} if the citaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the citaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the citaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/citas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitaDTO> partialUpdateCita(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CitaDTO citaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cita partially : {}, {}", id, citaDTO);
        if (citaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitaDTO> result = citaService.partialUpdate(citaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /citas} : get all the citas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citas in body.
     */
    @GetMapping("/citas")
    public List<CitaDTO> getAllCitas() {
        log.debug("REST request to get all Citas");
        return citaService.findAll();
    }

    /**
     * {@code GET  /citas/:id} : get the "id" cita.
     *
     * @param id the id of the citaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/citas/{id}")
    public ResponseEntity<CitaDTO> getCita(@PathVariable Long id) {
        log.debug("REST request to get Cita : {}", id);
        Optional<CitaDTO> citaDTO = citaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citaDTO);
    }

    /**
     * {@code DELETE  /citas/:id} : delete the "id" cita.
     *
     * @param id the id of the citaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/citas/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        log.debug("REST request to delete Cita : {}", id);
        citaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
