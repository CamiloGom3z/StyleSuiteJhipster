package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.CargoRepository;
import com.pruebaproyecto.app.service.CargoService;
import com.pruebaproyecto.app.service.dto.CargoDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Cargo}.
 */
@RestController
@RequestMapping("/api")
public class CargoResource {

    private final Logger log = LoggerFactory.getLogger(CargoResource.class);

    private static final String ENTITY_NAME = "cargo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoService cargoService;

    private final CargoRepository cargoRepository;

    public CargoResource(CargoService cargoService, CargoRepository cargoRepository) {
        this.cargoService = cargoService;
        this.cargoRepository = cargoRepository;
    }

    /**
     * {@code POST  /cargos} : Create a new cargo.
     *
     * @param cargoDTO the cargoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoDTO, or with status {@code 400 (Bad Request)} if the cargo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cargos")
    public ResponseEntity<CargoDTO> createCargo(@RequestBody CargoDTO cargoDTO) throws URISyntaxException {
        log.debug("REST request to save Cargo : {}", cargoDTO);
        if (cargoDTO.getId() != null) {
            throw new BadRequestAlertException("A new cargo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoDTO result = cargoService.save(cargoDTO);
        return ResponseEntity
            .created(new URI("/api/cargos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cargos/:id} : Updates an existing cargo.
     *
     * @param id the id of the cargoDTO to save.
     * @param cargoDTO the cargoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoDTO,
     * or with status {@code 400 (Bad Request)} if the cargoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cargos/{id}")
    public ResponseEntity<CargoDTO> updateCargo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoDTO cargoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cargo : {}, {}", id, cargoDTO);
        if (cargoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CargoDTO result = cargoService.update(cargoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cargos/:id} : Partial updates given fields of an existing cargo, field will ignore if it is null
     *
     * @param id the id of the cargoDTO to save.
     * @param cargoDTO the cargoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoDTO,
     * or with status {@code 400 (Bad Request)} if the cargoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cargoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cargos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoDTO> partialUpdateCargo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoDTO cargoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cargo partially : {}, {}", id, cargoDTO);
        if (cargoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoDTO> result = cargoService.partialUpdate(cargoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cargos} : get all the cargos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargos in body.
     */
    @GetMapping("/cargos")
    public List<CargoDTO> getAllCargos() {
        log.debug("REST request to get all Cargos");
        return cargoService.findAll();
    }

    /**
     * {@code GET  /cargos/:id} : get the "id" cargo.
     *
     * @param id the id of the cargoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cargos/{id}")
    public ResponseEntity<CargoDTO> getCargo(@PathVariable Long id) {
        log.debug("REST request to get Cargo : {}", id);
        Optional<CargoDTO> cargoDTO = cargoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoDTO);
    }

    /**
     * {@code DELETE  /cargos/:id} : delete the "id" cargo.
     *
     * @param id the id of the cargoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cargos/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Long id) {
        log.debug("REST request to delete Cargo : {}", id);
        cargoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
