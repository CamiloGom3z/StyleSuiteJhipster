package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.AgendaRepository;
import com.pruebaproyecto.app.service.AgendaService;
import com.pruebaproyecto.app.service.dto.AgendaDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Agenda}.
 */
@RestController
@RequestMapping("/api")
public class AgendaResource {

    private final Logger log = LoggerFactory.getLogger(AgendaResource.class);

    private static final String ENTITY_NAME = "agenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaService agendaService;

    private final AgendaRepository agendaRepository;

    public AgendaResource(AgendaService agendaService, AgendaRepository agendaRepository) {
        this.agendaService = agendaService;
        this.agendaRepository = agendaRepository;
    }

    /**
     * {@code POST  /agenda} : Create a new agenda.
     *
     * @param agendaDTO the agendaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendaDTO, or with status {@code 400 (Bad Request)} if the agenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenda")
    public ResponseEntity<AgendaDTO> createAgenda(@RequestBody AgendaDTO agendaDTO) throws URISyntaxException {
        log.debug("REST request to save Agenda : {}", agendaDTO);
        if (agendaDTO.getId() != null) {
            throw new BadRequestAlertException("A new agenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgendaDTO result = agendaService.save(agendaDTO);
        return ResponseEntity
            .created(new URI("/api/agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenda/:id} : Updates an existing agenda.
     *
     * @param id the id of the agendaDTO to save.
     * @param agendaDTO the agendaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaDTO,
     * or with status {@code 400 (Bad Request)} if the agendaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenda/{id}")
    public ResponseEntity<AgendaDTO> updateAgenda(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaDTO agendaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Agenda : {}, {}", id, agendaDTO);
        if (agendaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgendaDTO result = agendaService.update(agendaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agendaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agenda/:id} : Partial updates given fields of an existing agenda, field will ignore if it is null
     *
     * @param id the id of the agendaDTO to save.
     * @param agendaDTO the agendaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaDTO,
     * or with status {@code 400 (Bad Request)} if the agendaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agendaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agendaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agenda/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgendaDTO> partialUpdateAgenda(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaDTO agendaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agenda partially : {}, {}", id, agendaDTO);
        if (agendaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgendaDTO> result = agendaService.partialUpdate(agendaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agendaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda} : get all the agenda.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agenda in body.
     */
    @GetMapping("/agenda")
    public List<AgendaDTO> getAllAgenda() {
        log.debug("REST request to get all Agenda");
        return agendaService.findAll();
    }

    /**
     * {@code GET  /agenda/:id} : get the "id" agenda.
     *
     * @param id the id of the agendaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenda/{id}")
    public ResponseEntity<AgendaDTO> getAgenda(@PathVariable Long id) {
        log.debug("REST request to get Agenda : {}", id);
        Optional<AgendaDTO> agendaDTO = agendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agendaDTO);
    }

    /**
     * {@code DELETE  /agenda/:id} : delete the "id" agenda.
     *
     * @param id the id of the agendaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenda/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        log.debug("REST request to delete Agenda : {}", id);
        agendaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
