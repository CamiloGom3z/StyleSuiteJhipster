package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.TipoServcioRepository;
import com.pruebaproyecto.app.service.TipoServcioService;
import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.TipoServcio}.
 */
@RestController
@RequestMapping("/api")
public class TipoServcioResource {

    private final Logger log = LoggerFactory.getLogger(TipoServcioResource.class);

    private static final String ENTITY_NAME = "tipoServcio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoServcioService tipoServcioService;

    private final TipoServcioRepository tipoServcioRepository;

    public TipoServcioResource(TipoServcioService tipoServcioService, TipoServcioRepository tipoServcioRepository) {
        this.tipoServcioService = tipoServcioService;
        this.tipoServcioRepository = tipoServcioRepository;
    }

    /**
     * {@code POST  /tipo-servcios} : Create a new tipoServcio.
     *
     * @param tipoServcioDTO the tipoServcioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoServcioDTO, or with status {@code 400 (Bad Request)} if the tipoServcio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-servcios")
    public ResponseEntity<TipoServcioDTO> createTipoServcio(@RequestBody TipoServcioDTO tipoServcioDTO) throws URISyntaxException {
        log.debug("REST request to save TipoServcio : {}", tipoServcioDTO);
        if (tipoServcioDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoServcio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoServcioDTO result = tipoServcioService.save(tipoServcioDTO);
        return ResponseEntity
            .created(new URI("/api/tipo-servcios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-servcios/:id} : Updates an existing tipoServcio.
     *
     * @param id the id of the tipoServcioDTO to save.
     * @param tipoServcioDTO the tipoServcioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoServcioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoServcioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoServcioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-servcios/{id}")
    public ResponseEntity<TipoServcioDTO> updateTipoServcio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoServcioDTO tipoServcioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TipoServcio : {}, {}", id, tipoServcioDTO);
        if (tipoServcioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoServcioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoServcioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoServcioDTO result = tipoServcioService.update(tipoServcioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoServcioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-servcios/:id} : Partial updates given fields of an existing tipoServcio, field will ignore if it is null
     *
     * @param id the id of the tipoServcioDTO to save.
     * @param tipoServcioDTO the tipoServcioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoServcioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoServcioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoServcioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoServcioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-servcios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoServcioDTO> partialUpdateTipoServcio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoServcioDTO tipoServcioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoServcio partially : {}, {}", id, tipoServcioDTO);
        if (tipoServcioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoServcioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoServcioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoServcioDTO> result = tipoServcioService.partialUpdate(tipoServcioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoServcioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-servcios} : get all the tipoServcios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoServcios in body.
     */
    @GetMapping("/tipo-servcios")
    public List<TipoServcioDTO> getAllTipoServcios() {
        log.debug("REST request to get all TipoServcios");
        return tipoServcioService.findAll();
    }

    /**
     * {@code GET  /tipo-servcios/:id} : get the "id" tipoServcio.
     *
     * @param id the id of the tipoServcioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoServcioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-servcios/{id}")
    public ResponseEntity<TipoServcioDTO> getTipoServcio(@PathVariable Long id) {
        log.debug("REST request to get TipoServcio : {}", id);
        Optional<TipoServcioDTO> tipoServcioDTO = tipoServcioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoServcioDTO);
    }

    /**
     * {@code DELETE  /tipo-servcios/:id} : delete the "id" tipoServcio.
     *
     * @param id the id of the tipoServcioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-servcios/{id}")
    public ResponseEntity<Void> deleteTipoServcio(@PathVariable Long id) {
        log.debug("REST request to delete TipoServcio : {}", id);
        tipoServcioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
