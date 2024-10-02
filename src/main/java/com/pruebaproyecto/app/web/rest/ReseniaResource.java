package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.ReseniaRepository;
import com.pruebaproyecto.app.service.ReseniaService;
import com.pruebaproyecto.app.service.dto.ReseniaDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Resenia}.
 */
@RestController
@RequestMapping("/api")
public class ReseniaResource {

    private final Logger log = LoggerFactory.getLogger(ReseniaResource.class);

    private static final String ENTITY_NAME = "resenia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReseniaService reseniaService;

    private final ReseniaRepository reseniaRepository;

    public ReseniaResource(ReseniaService reseniaService, ReseniaRepository reseniaRepository) {
        this.reseniaService = reseniaService;
        this.reseniaRepository = reseniaRepository;
    }

    /**
     * {@code POST  /resenias} : Create a new resenia.
     *
     * @param reseniaDTO the reseniaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reseniaDTO, or with status {@code 400 (Bad Request)} if the resenia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resenias")
    public ResponseEntity<ReseniaDTO> createResenia(@RequestBody ReseniaDTO reseniaDTO) throws URISyntaxException {
        log.debug("REST request to save Resenia : {}", reseniaDTO);
        if (reseniaDTO.getId() != null) {
            throw new BadRequestAlertException("A new resenia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReseniaDTO result = reseniaService.save(reseniaDTO);
        return ResponseEntity
            .created(new URI("/api/resenias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resenias/:id} : Updates an existing resenia.
     *
     * @param id the id of the reseniaDTO to save.
     * @param reseniaDTO the reseniaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reseniaDTO,
     * or with status {@code 400 (Bad Request)} if the reseniaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reseniaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resenias/{id}")
    public ResponseEntity<ReseniaDTO> updateResenia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReseniaDTO reseniaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Resenia : {}, {}", id, reseniaDTO);
        if (reseniaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reseniaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reseniaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReseniaDTO result = reseniaService.update(reseniaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reseniaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resenias/:id} : Partial updates given fields of an existing resenia, field will ignore if it is null
     *
     * @param id the id of the reseniaDTO to save.
     * @param reseniaDTO the reseniaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reseniaDTO,
     * or with status {@code 400 (Bad Request)} if the reseniaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reseniaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reseniaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resenias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReseniaDTO> partialUpdateResenia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReseniaDTO reseniaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resenia partially : {}, {}", id, reseniaDTO);
        if (reseniaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reseniaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reseniaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReseniaDTO> result = reseniaService.partialUpdate(reseniaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reseniaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resenias} : get all the resenias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resenias in body.
     */
    @GetMapping("/resenias")
    public List<ReseniaDTO> getAllResenias() {
        log.debug("REST request to get all Resenias");
        return reseniaService.findAll();
    }

    /**
     * {@code GET  /resenias/:id} : get the "id" resenia.
     *
     * @param id the id of the reseniaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reseniaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resenias/{id}")
    public ResponseEntity<ReseniaDTO> getResenia(@PathVariable Long id) {
        log.debug("REST request to get Resenia : {}", id);
        Optional<ReseniaDTO> reseniaDTO = reseniaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reseniaDTO);
    }

    /**
     * {@code DELETE  /resenias/:id} : delete the "id" resenia.
     *
     * @param id the id of the reseniaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resenias/{id}")
    public ResponseEntity<Void> deleteResenia(@PathVariable Long id) {
        log.debug("REST request to delete Resenia : {}", id);
        reseniaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
