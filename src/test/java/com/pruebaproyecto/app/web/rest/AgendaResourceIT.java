package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Agenda;
import com.pruebaproyecto.app.repository.AgendaRepository;
import com.pruebaproyecto.app.service.dto.AgendaDTO;
import com.pruebaproyecto.app.service.mapper.AgendaMapper;
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
 * Integration tests for the {@link AgendaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgendaResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DISPONIBLE = false;
    private static final Boolean UPDATED_DISPONIBLE = true;

    private static final String ENTITY_API_URL = "/api/agenda";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaMapper agendaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaMockMvc;

    private Agenda agenda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createEntity(EntityManager em) {
        Agenda agenda = new Agenda().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).disponible(DEFAULT_DISPONIBLE);
        return agenda;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createUpdatedEntity(EntityManager em) {
        Agenda agenda = new Agenda().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).disponible(UPDATED_DISPONIBLE);
        return agenda;
    }

    @BeforeEach
    public void initTest() {
        agenda = createEntity(em);
    }

    @Test
    @Transactional
    void createAgenda() throws Exception {
        int databaseSizeBeforeCreate = agendaRepository.findAll().size();
        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);
        restAgendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaDTO)))
            .andExpect(status().isCreated());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate + 1);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testAgenda.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testAgenda.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void createAgendaWithExistingId() throws Exception {
        // Create the Agenda with an existing ID
        agenda.setId(1L);
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        int databaseSizeBeforeCreate = agendaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].disponible").value(hasItem(DEFAULT_DISPONIBLE.booleanValue())));
    }

    @Test
    @Transactional
    void getAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get the agenda
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL_ID, agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agenda.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.disponible").value(DEFAULT_DISPONIBLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAgenda() throws Exception {
        // Get the agenda
        restAgendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda
        Agenda updatedAgenda = agendaRepository.findById(agenda.getId()).get();
        // Disconnect from session so that the updates on updatedAgenda are not directly saved in db
        em.detach(updatedAgenda);
        updatedAgenda.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).disponible(UPDATED_DISPONIBLE);
        AgendaDTO agendaDTO = agendaMapper.toDto(updatedAgenda);

        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testAgenda.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testAgenda.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void putNonExistingAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agendaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.fechaInicio(UPDATED_FECHA_INICIO);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testAgenda.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testAgenda.getDisponible()).isEqualTo(DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void fullUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).disponible(UPDATED_DISPONIBLE);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testAgenda.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testAgenda.getDisponible()).isEqualTo(UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agendaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // Create the Agenda
        AgendaDTO agendaDTO = agendaMapper.toDto(agenda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(agendaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeDelete = agendaRepository.findAll().size();

        // Delete the agenda
        restAgendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, agenda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
