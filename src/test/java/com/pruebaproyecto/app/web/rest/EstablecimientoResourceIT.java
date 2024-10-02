package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Establecimiento;
import com.pruebaproyecto.app.repository.EstablecimientoRepository;
import com.pruebaproyecto.app.service.dto.EstablecimientoDTO;
import com.pruebaproyecto.app.service.mapper.EstablecimientoMapper;
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
 * Integration tests for the {@link EstablecimientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EstablecimientoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Long DEFAULT_NIT = 1L;
    private static final Long UPDATED_NIT = 2L;

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO_ELECTRONICO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO_ELECTRONICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/establecimientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    @Autowired
    private EstablecimientoMapper establecimientoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstablecimientoMockMvc;

    private Establecimiento establecimiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Establecimiento createEntity(EntityManager em) {
        Establecimiento establecimiento = new Establecimiento()
            .nombre(DEFAULT_NOMBRE)
            .nit(DEFAULT_NIT)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .correoElectronico(DEFAULT_CORREO_ELECTRONICO);
        return establecimiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Establecimiento createUpdatedEntity(EntityManager em) {
        Establecimiento establecimiento = new Establecimiento()
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO);
        return establecimiento;
    }

    @BeforeEach
    public void initTest() {
        establecimiento = createEntity(em);
    }

    @Test
    @Transactional
    void createEstablecimiento() throws Exception {
        int databaseSizeBeforeCreate = establecimientoRepository.findAll().size();
        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);
        restEstablecimientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeCreate + 1);
        Establecimiento testEstablecimiento = establecimientoList.get(establecimientoList.size() - 1);
        assertThat(testEstablecimiento.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEstablecimiento.getNit()).isEqualTo(DEFAULT_NIT);
        assertThat(testEstablecimiento.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testEstablecimiento.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testEstablecimiento.getCorreoElectronico()).isEqualTo(DEFAULT_CORREO_ELECTRONICO);
    }

    @Test
    @Transactional
    void createEstablecimientoWithExistingId() throws Exception {
        // Create the Establecimiento with an existing ID
        establecimiento.setId(1L);
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        int databaseSizeBeforeCreate = establecimientoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstablecimientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEstablecimientos() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        // Get all the establecimientoList
        restEstablecimientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(establecimiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT.intValue())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].correoElectronico").value(hasItem(DEFAULT_CORREO_ELECTRONICO)));
    }

    @Test
    @Transactional
    void getEstablecimiento() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        // Get the establecimiento
        restEstablecimientoMockMvc
            .perform(get(ENTITY_API_URL_ID, establecimiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(establecimiento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT.intValue()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.correoElectronico").value(DEFAULT_CORREO_ELECTRONICO));
    }

    @Test
    @Transactional
    void getNonExistingEstablecimiento() throws Exception {
        // Get the establecimiento
        restEstablecimientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEstablecimiento() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();

        // Update the establecimiento
        Establecimiento updatedEstablecimiento = establecimientoRepository.findById(establecimiento.getId()).get();
        // Disconnect from session so that the updates on updatedEstablecimiento are not directly saved in db
        em.detach(updatedEstablecimiento);
        updatedEstablecimiento
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO);
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(updatedEstablecimiento);

        restEstablecimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, establecimientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
        Establecimiento testEstablecimiento = establecimientoList.get(establecimientoList.size() - 1);
        assertThat(testEstablecimiento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstablecimiento.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEstablecimiento.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEstablecimiento.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstablecimiento.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
    }

    @Test
    @Transactional
    void putNonExistingEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, establecimientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstablecimientoWithPatch() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();

        // Update the establecimiento using partial update
        Establecimiento partialUpdatedEstablecimiento = new Establecimiento();
        partialUpdatedEstablecimiento.setId(establecimiento.getId());

        partialUpdatedEstablecimiento
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO);

        restEstablecimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstablecimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstablecimiento))
            )
            .andExpect(status().isOk());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
        Establecimiento testEstablecimiento = establecimientoList.get(establecimientoList.size() - 1);
        assertThat(testEstablecimiento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstablecimiento.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEstablecimiento.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEstablecimiento.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstablecimiento.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
    }

    @Test
    @Transactional
    void fullUpdateEstablecimientoWithPatch() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();

        // Update the establecimiento using partial update
        Establecimiento partialUpdatedEstablecimiento = new Establecimiento();
        partialUpdatedEstablecimiento.setId(establecimiento.getId());

        partialUpdatedEstablecimiento
            .nombre(UPDATED_NOMBRE)
            .nit(UPDATED_NIT)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .correoElectronico(UPDATED_CORREO_ELECTRONICO);

        restEstablecimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstablecimiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEstablecimiento))
            )
            .andExpect(status().isOk());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
        Establecimiento testEstablecimiento = establecimientoList.get(establecimientoList.size() - 1);
        assertThat(testEstablecimiento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEstablecimiento.getNit()).isEqualTo(UPDATED_NIT);
        assertThat(testEstablecimiento.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEstablecimiento.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testEstablecimiento.getCorreoElectronico()).isEqualTo(UPDATED_CORREO_ELECTRONICO);
    }

    @Test
    @Transactional
    void patchNonExistingEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, establecimientoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstablecimiento() throws Exception {
        int databaseSizeBeforeUpdate = establecimientoRepository.findAll().size();
        establecimiento.setId(count.incrementAndGet());

        // Create the Establecimiento
        EstablecimientoDTO establecimientoDTO = establecimientoMapper.toDto(establecimiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstablecimientoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(establecimientoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Establecimiento in the database
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstablecimiento() throws Exception {
        // Initialize the database
        establecimientoRepository.saveAndFlush(establecimiento);

        int databaseSizeBeforeDelete = establecimientoRepository.findAll().size();

        // Delete the establecimiento
        restEstablecimientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, establecimiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Establecimiento> establecimientoList = establecimientoRepository.findAll();
        assertThat(establecimientoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
