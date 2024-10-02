package com.pruebaproyecto.app.web.rest;

import com.pruebaproyecto.app.repository.ProductosRepository;
import com.pruebaproyecto.app.service.ProductosService;
import com.pruebaproyecto.app.service.dto.ProductosDTO;
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
 * REST controller for managing {@link com.pruebaproyecto.app.domain.Productos}.
 */
@RestController
@RequestMapping("/api")
public class ProductosResource {

    private final Logger log = LoggerFactory.getLogger(ProductosResource.class);

    private static final String ENTITY_NAME = "productos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductosService productosService;

    private final ProductosRepository productosRepository;

    public ProductosResource(ProductosService productosService, ProductosRepository productosRepository) {
        this.productosService = productosService;
        this.productosRepository = productosRepository;
    }

    /**
     * {@code POST  /productos} : Create a new productos.
     *
     * @param productosDTO the productosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productosDTO, or with status {@code 400 (Bad Request)} if the productos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<ProductosDTO> createProductos(@RequestBody ProductosDTO productosDTO) throws URISyntaxException {
        log.debug("REST request to save Productos : {}", productosDTO);
        if (productosDTO.getId() != null) {
            throw new BadRequestAlertException("A new productos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductosDTO result = productosService.save(productosDTO);
        return ResponseEntity
            .created(new URI("/api/productos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /productos/:id} : Updates an existing productos.
     *
     * @param id the id of the productosDTO to save.
     * @param productosDTO the productosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productosDTO,
     * or with status {@code 400 (Bad Request)} if the productosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductosDTO> updateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductosDTO productosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Productos : {}, {}", id, productosDTO);
        if (productosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductosDTO result = productosService.update(productosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /productos/:id} : Partial updates given fields of an existing productos, field will ignore if it is null
     *
     * @param id the id of the productosDTO to save.
     * @param productosDTO the productosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productosDTO,
     * or with status {@code 400 (Bad Request)} if the productosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/productos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductosDTO> partialUpdateProductos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductosDTO productosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Productos partially : {}, {}", id, productosDTO);
        if (productosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductosDTO> result = productosService.partialUpdate(productosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public List<ProductosDTO> getAllProductos(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Productos");
        return productosService.findAll();
    }

    /**
     * {@code GET  /productos/:id} : get the "id" productos.
     *
     * @param id the id of the productosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductosDTO> getProductos(@PathVariable Long id) {
        log.debug("REST request to get Productos : {}", id);
        Optional<ProductosDTO> productosDTO = productosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productosDTO);
    }

    /**
     * {@code DELETE  /productos/:id} : delete the "id" productos.
     *
     * @param id the id of the productosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProductos(@PathVariable Long id) {
        log.debug("REST request to delete Productos : {}", id);
        productosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
