package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.PromocionRepository;
import com.pruebaproyecto.app.service.PromocionService;
import com.pruebaproyecto.app.service.dto.PromocionDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Promocion}.
 */
@RestController
@RequestMapping("/api")
public class PromocionResource {

    private final Logger log = LoggerFactory.getLogger(PromocionResource.class);

    private static final String ENTITY_NAME = "promocion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromocionService promocionService;

    private final PromocionRepository promocionRepository;

    public PromocionResource(PromocionService promocionService, PromocionRepository promocionRepository) {
        this.promocionService = promocionService;
        this.promocionRepository = promocionRepository;
    }

    /**
     * {@code POST  /promocions} : Create a new promocion.
     *
     * @param promocionDTO the promocionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promocionDTO, or with status {@code 400 (Bad Request)} if the promocion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promocions")
    public ResponseEntity<PromocionDTO> createPromocion(@RequestBody PromocionDTO promocionDTO) throws URISyntaxException {
        log.debug("REST request to save Promocion : {}", promocionDTO);
        if (promocionDTO.getId() != null) {
            throw new BadRequestAlertException("A new promocion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PromocionDTO result = promocionService.save(promocionDTO);
        return ResponseEntity
            .created(new URI("/api/promocions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promocions/:id} : Updates an existing promocion.
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promocions/{id}")
    public ResponseEntity<PromocionDTO> updatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Promocion : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PromocionDTO result = promocionService.update(promocionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promocionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promocions/:id} : Partial updates given fields of an existing promocion, field will ignore if it is null
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promocionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promocions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PromocionDTO> partialUpdatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Promocion partially : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PromocionDTO> result = promocionService.partialUpdate(promocionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, promocionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /promocions} : get all the promocions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promocions in body.
     */
    @GetMapping("/promocions")
    public List<PromocionDTO> getAllPromocions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Promocions");
        return promocionService.findAll();
    }

    /**
     * {@code GET  /promocions/:id} : get the "id" promocion.
     *
     * @param id the id of the promocionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promocionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promocions/{id}")
    public ResponseEntity<PromocionDTO> getPromocion(@PathVariable Long id) {
        log.debug("REST request to get Promocion : {}", id);
        Optional<PromocionDTO> promocionDTO = promocionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promocionDTO);
    }

    /**
     * {@code DELETE  /promocions/:id} : delete the "id" promocion.
     *
     * @param id the id of the promocionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promocions/{id}")
    public ResponseEntity<Void> deletePromocion(@PathVariable Long id) {
        log.debug("REST request to delete Promocion : {}", id);
        promocionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
