package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.AgendaEmpleadoRepository;
import com.pruebaproyecto.app.service.AgendaEmpleadoService;
import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import com.pruebaproyecto.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pruebaproyecto.app.domain.AgendaEmpleado}.
 */
@RestController
@RequestMapping("/api")
public class AgendaEmpleadoResource {

    private final Logger log = LoggerFactory.getLogger(AgendaEmpleadoResource.class);

    private static final String ENTITY_NAME = "agendaEmpleado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaEmpleadoService agendaEmpleadoService;

    private final AgendaEmpleadoRepository agendaEmpleadoRepository;

    public AgendaEmpleadoResource(AgendaEmpleadoService agendaEmpleadoService, AgendaEmpleadoRepository agendaEmpleadoRepository) {
        this.agendaEmpleadoService = agendaEmpleadoService;
        this.agendaEmpleadoRepository = agendaEmpleadoRepository;
    }

    /**
     * {@code POST  /agenda-empleados} : Create a new agendaEmpleado.
     *
     * @param agendaEmpleadoDTO the agendaEmpleadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendaEmpleadoDTO, or with status {@code 400 (Bad Request)} if the agendaEmpleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenda-empleados")
    public ResponseEntity<AgendaEmpleadoDTO> createAgendaEmpleado(@RequestBody AgendaEmpleadoDTO agendaEmpleadoDTO)
        throws URISyntaxException {
        log.debug("REST request to save AgendaEmpleado : {}", agendaEmpleadoDTO);
        if (agendaEmpleadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new agendaEmpleado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgendaEmpleadoDTO result = agendaEmpleadoService.save(agendaEmpleadoDTO);
        return ResponseEntity
            .created(new URI("/api/agenda-empleados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenda-empleados/:id} : Updates an existing agendaEmpleado.
     *
     * @param id the id of the agendaEmpleadoDTO to save.
     * @param agendaEmpleadoDTO the agendaEmpleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaEmpleadoDTO,
     * or with status {@code 400 (Bad Request)} if the agendaEmpleadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendaEmpleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenda-empleados/{id}")
    public ResponseEntity<AgendaEmpleadoDTO> updateAgendaEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaEmpleadoDTO agendaEmpleadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgendaEmpleado : {}, {}", id, agendaEmpleadoDTO);
        if (agendaEmpleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaEmpleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaEmpleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgendaEmpleadoDTO result = agendaEmpleadoService.update(agendaEmpleadoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agendaEmpleadoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agenda-empleados/:id} : Partial updates given fields of an existing agendaEmpleado, field will ignore if it is null
     *
     * @param id the id of the agendaEmpleadoDTO to save.
     * @param agendaEmpleadoDTO the agendaEmpleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaEmpleadoDTO,
     * or with status {@code 400 (Bad Request)} if the agendaEmpleadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agendaEmpleadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agendaEmpleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agenda-empleados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgendaEmpleadoDTO> partialUpdateAgendaEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaEmpleadoDTO agendaEmpleadoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgendaEmpleado partially : {}, {}", id, agendaEmpleadoDTO);
        if (agendaEmpleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaEmpleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaEmpleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgendaEmpleadoDTO> result = agendaEmpleadoService.partialUpdate(agendaEmpleadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agendaEmpleadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda-empleados} : get all the agendaEmpleados.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendaEmpleados in body.
     */
    @GetMapping("/agenda-empleados")
    public List<AgendaEmpleadoDTO> getAllAgendaEmpleados(@RequestParam(required = false) String filter) {
        if ("cita-is-null".equals(filter)) {
            log.debug("REST request to get all AgendaEmpleados where cita is null");
            return agendaEmpleadoService.findAllWhereCitaIsNull();
        }
        log.debug("REST request to get all AgendaEmpleados");
        return agendaEmpleadoService.findAll();
    }

    /**
     * {@code GET  /agenda-empleados/:id} : get the "id" agendaEmpleado.
     *
     * @param id the id of the agendaEmpleadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaEmpleadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenda-empleados/{id}")
    public ResponseEntity<AgendaEmpleadoDTO> getAgendaEmpleado(@PathVariable Long id) {
        log.debug("REST request to get AgendaEmpleado : {}", id);
        Optional<AgendaEmpleadoDTO> agendaEmpleadoDTO = agendaEmpleadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agendaEmpleadoDTO);
    }

    /**
     * {@code DELETE  /agenda-empleados/:id} : delete the "id" agendaEmpleado.
     *
     * @param id the id of the agendaEmpleadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenda-empleados/{id}")
    public ResponseEntity<Void> deleteAgendaEmpleado(@PathVariable Long id) {
        log.debug("REST request to delete AgendaEmpleado : {}", id);
        agendaEmpleadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
