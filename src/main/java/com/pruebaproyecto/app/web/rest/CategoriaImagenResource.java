package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.CategoriaImagenRepository;
import com.pruebaproyecto.app.service.CategoriaImagenService;
import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.CategoriaImagen}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaImagenResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaImagenResource.class);

    private static final String ENTITY_NAME = "categoriaImagen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaImagenService categoriaImagenService;

    private final CategoriaImagenRepository categoriaImagenRepository;

    public CategoriaImagenResource(CategoriaImagenService categoriaImagenService, CategoriaImagenRepository categoriaImagenRepository) {
        this.categoriaImagenService = categoriaImagenService;
        this.categoriaImagenRepository = categoriaImagenRepository;
    }

    /**
     * {@code POST  /categoria-imagens} : Create a new categoriaImagen.
     *
     * @param categoriaImagenDTO the categoriaImagenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaImagenDTO, or with status {@code 400 (Bad Request)} if the categoriaImagen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categoria-imagens")
    public ResponseEntity<CategoriaImagenDTO> createCategoriaImagen(@RequestBody CategoriaImagenDTO categoriaImagenDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoriaImagen : {}", categoriaImagenDTO);
        if (categoriaImagenDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaImagen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoriaImagenDTO result = categoriaImagenService.save(categoriaImagenDTO);
        return ResponseEntity
            .created(new URI("/api/categoria-imagens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categoria-imagens/:id} : Updates an existing categoriaImagen.
     *
     * @param id the id of the categoriaImagenDTO to save.
     * @param categoriaImagenDTO the categoriaImagenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaImagenDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaImagenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaImagenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categoria-imagens/{id}")
    public ResponseEntity<CategoriaImagenDTO> updateCategoriaImagen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaImagenDTO categoriaImagenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoriaImagen : {}, {}", id, categoriaImagenDTO);
        if (categoriaImagenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaImagenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaImagenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoriaImagenDTO result = categoriaImagenService.update(categoriaImagenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaImagenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categoria-imagens/:id} : Partial updates given fields of an existing categoriaImagen, field will ignore if it is null
     *
     * @param id the id of the categoriaImagenDTO to save.
     * @param categoriaImagenDTO the categoriaImagenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaImagenDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaImagenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaImagenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaImagenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categoria-imagens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoriaImagenDTO> partialUpdateCategoriaImagen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaImagenDTO categoriaImagenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoriaImagen partially : {}, {}", id, categoriaImagenDTO);
        if (categoriaImagenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaImagenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaImagenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaImagenDTO> result = categoriaImagenService.partialUpdate(categoriaImagenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaImagenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categoria-imagens} : get all the categoriaImagens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoriaImagens in body.
     */
    @GetMapping("/categoria-imagens")
    public List<CategoriaImagenDTO> getAllCategoriaImagens() {
        log.debug("REST request to get all CategoriaImagens");
        return categoriaImagenService.findAll();
    }

    /**
     * {@code GET  /categoria-imagens/:id} : get the "id" categoriaImagen.
     *
     * @param id the id of the categoriaImagenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaImagenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categoria-imagens/{id}")
    public ResponseEntity<CategoriaImagenDTO> getCategoriaImagen(@PathVariable Long id) {
        log.debug("REST request to get CategoriaImagen : {}", id);
        Optional<CategoriaImagenDTO> categoriaImagenDTO = categoriaImagenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaImagenDTO);
    }

    /**
     * {@code DELETE  /categoria-imagens/:id} : delete the "id" categoriaImagen.
     *
     * @param id the id of the categoriaImagenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categoria-imagens/{id}")
    public ResponseEntity<Void> deleteCategoriaImagen(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaImagen : {}", id);
        categoriaImagenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
