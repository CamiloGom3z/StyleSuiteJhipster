package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Cargo;
import com.pruebaproyecto.app.repository.CargoRepository;
import com.pruebaproyecto.app.service.dto.CargoDTO;
import com.pruebaproyecto.app.service.mapper.CargoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CargoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cargos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CargoMapper cargoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoMockMvc;

    private Cargo cargo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cargo createEntity(EntityManager em) {
        Cargo cargo = new Cargo().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return cargo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cargo createUpdatedEntity(EntityManager em) {
        Cargo cargo = new Cargo().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return cargo;
    }

    @BeforeEach
    public void initTest() {
        cargo = createEntity(em);
    }

    @Test
    @Transactional
    void createCargo() throws Exception {
        int databaseSizeBeforeCreate = cargoRepository.findAll().size();
        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);
        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoDTO)))
            .andExpect(status().isCreated());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeCreate + 1);
        Cargo testCargo = cargoList.get(cargoList.size() - 1);
        assertThat(testCargo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCargo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCargoWithExistingId() throws Exception {
        // Create the Cargo with an existing ID
        cargo.setId(1L);
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        int databaseSizeBeforeCreate = cargoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCargos() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        // Get all the cargoList
        restCargoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCargo() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        // Get the cargo
        restCargoMockMvc
            .perform(get(ENTITY_API_URL_ID, cargo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCargo() throws Exception {
        // Get the cargo
        restCargoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCargo() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();

        // Update the cargo
        Cargo updatedCargo = cargoRepository.findById(cargo.getId()).get();
        // Disconnect from session so that the updates on updatedCargo are not directly saved in db
        em.detach(updatedCargo);
        updatedCargo.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        CargoDTO cargoDTO = cargoMapper.toDto(updatedCargo);

        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
        Cargo testCargo = cargoList.get(cargoList.size() - 1);
        assertThat(testCargo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCargo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoWithPatch() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();

        // Update the cargo using partial update
        Cargo partialUpdatedCargo = new Cargo();
        partialUpdatedCargo.setId(cargo.getId());

        partialUpdatedCargo.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargo))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
        Cargo testCargo = cargoList.get(cargoList.size() - 1);
        assertThat(testCargo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCargo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCargoWithPatch() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();

        // Update the cargo using partial update
        Cargo partialUpdatedCargo = new Cargo();
        partialUpdatedCargo.setId(cargo.getId());

        partialUpdatedCargo.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargo))
            )
            .andExpect(status().isOk());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
        Cargo testCargo = cargoList.get(cargoList.size() - 1);
        assertThat(testCargo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCargo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargo() throws Exception {
        int databaseSizeBeforeUpdate = cargoRepository.findAll().size();
        cargo.setId(count.incrementAndGet());

        // Create the Cargo
        CargoDTO cargoDTO = cargoMapper.toDto(cargo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cargoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cargo in the database
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargo() throws Exception {
        // Initialize the database
        cargoRepository.saveAndFlush(cargo);

        int databaseSizeBeforeDelete = cargoRepository.findAll().size();

        // Delete the cargo
        restCargoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cargo> cargoList = cargoRepository.findAll();
        assertThat(cargoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
