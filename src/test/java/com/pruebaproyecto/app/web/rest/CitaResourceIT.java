package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Cita;
import com.pruebaproyecto.app.domain.enumeration.EstadoCitaEnum;
import com.pruebaproyecto.app.repository.CitaRepository;
import com.pruebaproyecto.app.service.dto.CitaDTO;
import com.pruebaproyecto.app.service.mapper.CitaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitaResourceIT {

    private static final Instant DEFAULT_FECHA_CITA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CITA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN_CITA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN_CITA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EstadoCitaEnum DEFAULT_ESTADO = EstadoCitaEnum.PENDIENTE;
    private static final EstadoCitaEnum UPDATED_ESTADO = EstadoCitaEnum.CONFIRMADA;

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/citas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private CitaMapper citaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaMockMvc;

    private Cita cita;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity(EntityManager em) {
        Cita cita = new Cita()
            .fechaCita(DEFAULT_FECHA_CITA)
            .fechaFinCita(DEFAULT_FECHA_FIN_CITA)
            .estado(DEFAULT_ESTADO)
            .notas(DEFAULT_NOTAS);
        return cita;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createUpdatedEntity(EntityManager em) {
        Cita cita = new Cita()
            .fechaCita(UPDATED_FECHA_CITA)
            .fechaFinCita(UPDATED_FECHA_FIN_CITA)
            .estado(UPDATED_ESTADO)
            .notas(UPDATED_NOTAS);
        return cita;
    }

    @BeforeEach
    public void initTest() {
        cita = createEntity(em);
    }

    @Test
    @Transactional
    void createCita() throws Exception {
        int databaseSizeBeforeCreate = citaRepository.findAll().size();
        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaDTO)))
            .andExpect(status().isCreated());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate + 1);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaCita()).isEqualTo(DEFAULT_FECHA_CITA);
        assertThat(testCita.getFechaFinCita()).isEqualTo(DEFAULT_FECHA_FIN_CITA);
        assertThat(testCita.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCita.getNotas()).isEqualTo(DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void createCitaWithExistingId() throws Exception {
        // Create the Cita with an existing ID
        cita.setId(1L);
        CitaDTO citaDTO = citaMapper.toDto(cita);

        int databaseSizeBeforeCreate = citaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitas() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get all the citaList
        restCitaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cita.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCita").value(hasItem(DEFAULT_FECHA_CITA.toString())))
            .andExpect(jsonPath("$.[*].fechaFinCita").value(hasItem(DEFAULT_FECHA_FIN_CITA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @Test
    @Transactional
    void getCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        // Get the cita
        restCitaMockMvc
            .perform(get(ENTITY_API_URL_ID, cita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cita.getId().intValue()))
            .andExpect(jsonPath("$.fechaCita").value(DEFAULT_FECHA_CITA.toString()))
            .andExpect(jsonPath("$.fechaFinCita").value(DEFAULT_FECHA_FIN_CITA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getNonExistingCita() throws Exception {
        // Get the cita
        restCitaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita
        Cita updatedCita = citaRepository.findById(cita.getId()).get();
        // Disconnect from session so that the updates on updatedCita are not directly saved in db
        em.detach(updatedCita);
        updatedCita.fechaCita(UPDATED_FECHA_CITA).fechaFinCita(UPDATED_FECHA_FIN_CITA).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);
        CitaDTO citaDTO = citaMapper.toDto(updatedCita);

        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCita.getFechaFinCita()).isEqualTo(UPDATED_FECHA_FIN_CITA);
        assertThat(testCita.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCita.getNotas()).isEqualTo(UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void putNonExistingCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaCita(UPDATED_FECHA_CITA).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCita.getFechaFinCita()).isEqualTo(DEFAULT_FECHA_FIN_CITA);
        assertThat(testCita.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCita.getNotas()).isEqualTo(UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void fullUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeUpdate = citaRepository.findAll().size();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.fechaCita(UPDATED_FECHA_CITA).fechaFinCita(UPDATED_FECHA_FIN_CITA).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);

        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCita.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCita))
            )
            .andExpect(status().isOk());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
        Cita testCita = citaList.get(citaList.size() - 1);
        assertThat(testCita.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCita.getFechaFinCita()).isEqualTo(UPDATED_FECHA_FIN_CITA);
        assertThat(testCita.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCita.getNotas()).isEqualTo(UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void patchNonExistingCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCita() throws Exception {
        int databaseSizeBeforeUpdate = citaRepository.findAll().size();
        cita.setId(count.incrementAndGet());

        // Create the Cita
        CitaDTO citaDTO = citaMapper.toDto(cita);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(citaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cita in the database
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCita() throws Exception {
        // Initialize the database
        citaRepository.saveAndFlush(cita);

        int databaseSizeBeforeDelete = citaRepository.findAll().size();

        // Delete the cita
        restCitaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cita.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cita> citaList = citaRepository.findAll();
        assertThat(citaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
