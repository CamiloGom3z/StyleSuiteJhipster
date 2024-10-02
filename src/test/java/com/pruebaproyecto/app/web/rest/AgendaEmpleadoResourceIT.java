package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.AgendaEmpleado;
import com.pruebaproyecto.app.repository.AgendaEmpleadoRepository;
import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import com.pruebaproyecto.app.service.mapper.AgendaEmpleadoMapper;
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
 * Integration tests for the {@link AgendaEmpleadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgendaEmpleadoResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DISPONIBLE = false;
    private static final Boolean UPDATED_DISPONIBLE = true;

    private static final String ENTITY_API_URL = "/api/agenda-empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgendaEmpleadoRepository agendaEmpleadoRepository;

    @Autowired
    private AgendaEmpleadoMapper agendaEmpleadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaEmpleadoMockMvc;

    private AgendaEmpleado agendaEmpleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaEmpleado createEntity(EntityManager em) {
        AgendaEmpleado agendaEmpleado = new AgendaEmpleado()
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .disponible(DEFAULT_DISPONIBLE);
        return agendaEmpleado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaEmpleado createUpdatedEntity(EntityManager em) {
        AgendaEmpleado agendaEmpleado = new AgendaEmpleado()
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .disponible(UPDATED_DISPONIBLE);
        return agendaEmpleado;
    }

    @BeforeEach
    public void initTest() {
        agendaEmpleado = createEntity(em);
    }

    @Test
    @Transactional
    void createAgendaEmpleado() throws Exception {
        int databaseSizeBeforeCreate = agendaEmpleadoRepository.findAll().size();
        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);
        restAgendaEmpleadoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeCreate + 1);
        AgendaEmpleado testAgendaEmpleado = agendaEmpleadoList.get(agendaEmpleadoList.size() - 1);
        assertThat(testAgendaEmpleado.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testAgendaEmpleado.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testAgendaEmpleado.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void createAgendaEmpleadoWithExistingId() throws Exception {
        // Create the AgendaEmpleado with an existing ID
        agendaEmpleado.setId(1L);
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        int databaseSizeBeforeCreate = agendaEmpleadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaEmpleadoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgendaEmpleados() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        // Get all the agendaEmpleadoList
        restAgendaEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agendaEmpleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].disponible").value(hasItem(DEFAULT_DISPONIBLE.booleanValue())));
    }

    @Test
    @Transactional
    void getAgendaEmpleado() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        // Get the agendaEmpleado
        restAgendaEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, agendaEmpleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agendaEmpleado.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.disponible").value(DEFAULT_DISPONIBLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAgendaEmpleado() throws Exception {
        // Get the agendaEmpleado
        restAgendaEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgendaEmpleado() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();

        // Update the agendaEmpleado
        AgendaEmpleado updatedAgendaEmpleado = agendaEmpleadoRepository.findById(agendaEmpleado.getId()).get();
        // Disconnect from session so that the updates on updatedAgendaEmpleado are not directly saved in db
        em.detach(updatedAgendaEmpleado);
        updatedAgendaEmpleado.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).disponible(UPDATED_DISPONIBLE);
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(updatedAgendaEmpleado);

        restAgendaEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaEmpleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
        AgendaEmpleado testAgendaEmpleado = agendaEmpleadoList.get(agendaEmpleadoList.size() - 1);
        assertThat(testAgendaEmpleado.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testAgendaEmpleado.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testAgendaEmpleado.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void putNonExistingAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaEmpleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaEmpleadoWithPatch() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();

        // Update the agendaEmpleado using partial update
        AgendaEmpleado partialUpdatedAgendaEmpleado = new AgendaEmpleado();
        partialUpdatedAgendaEmpleado.setId(agendaEmpleado.getId());

        partialUpdatedAgendaEmpleado.disponible(UPDATED_DISPONIBLE);

        restAgendaEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgendaEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
        AgendaEmpleado testAgendaEmpleado = agendaEmpleadoList.get(agendaEmpleadoList.size() - 1);
        assertThat(testAgendaEmpleado.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testAgendaEmpleado.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testAgendaEmpleado.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void fullUpdateAgendaEmpleadoWithPatch() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();

        // Update the agendaEmpleado using partial update
        AgendaEmpleado partialUpdatedAgendaEmpleado = new AgendaEmpleado();
        partialUpdatedAgendaEmpleado.setId(agendaEmpleado.getId());

        partialUpdatedAgendaEmpleado.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).disponible(UPDATED_DISPONIBLE);

        restAgendaEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgendaEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
        AgendaEmpleado testAgendaEmpleado = agendaEmpleadoList.get(agendaEmpleadoList.size() - 1);
        assertThat(testAgendaEmpleado.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testAgendaEmpleado.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testAgendaEmpleado.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agendaEmpleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgendaEmpleado() throws Exception {
        int databaseSizeBeforeUpdate = agendaEmpleadoRepository.findAll().size();
        agendaEmpleado.setId(count.incrementAndGet());

        // Create the AgendaEmpleado
        AgendaEmpleadoDTO agendaEmpleadoDTO = agendaEmpleadoMapper.toDto(agendaEmpleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agendaEmpleadoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaEmpleado in the database
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgendaEmpleado() throws Exception {
        // Initialize the database
        agendaEmpleadoRepository.saveAndFlush(agendaEmpleado);

        int databaseSizeBeforeDelete = agendaEmpleadoRepository.findAll().size();

        // Delete the agendaEmpleado
        restAgendaEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, agendaEmpleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgendaEmpleado> agendaEmpleadoList = agendaEmpleadoRepository.findAll();
        assertThat(agendaEmpleadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
