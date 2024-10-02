package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Resenia;
import com.pruebaproyecto.app.repository.ReseniaRepository;
import com.pruebaproyecto.app.service.dto.ReseniaDTO;
import com.pruebaproyecto.app.service.mapper.ReseniaMapper;
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
 * Integration tests for the {@link ReseniaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReseniaResourceIT {

    private static final Integer DEFAULT_CALIFICACION = 1;
    private static final Integer UPDATED_CALIFICACION = 2;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/resenias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReseniaRepository reseniaRepository;

    @Autowired
    private ReseniaMapper reseniaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReseniaMockMvc;

    private Resenia resenia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resenia createEntity(EntityManager em) {
        Resenia resenia = new Resenia().calificacion(DEFAULT_CALIFICACION).comentario(DEFAULT_COMENTARIO).fecha(DEFAULT_FECHA);
        return resenia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resenia createUpdatedEntity(EntityManager em) {
        Resenia resenia = new Resenia().calificacion(UPDATED_CALIFICACION).comentario(UPDATED_COMENTARIO).fecha(UPDATED_FECHA);
        return resenia;
    }

    @BeforeEach
    public void initTest() {
        resenia = createEntity(em);
    }

    @Test
    @Transactional
    void createResenia() throws Exception {
        int databaseSizeBeforeCreate = reseniaRepository.findAll().size();
        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);
        restReseniaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reseniaDTO)))
            .andExpect(status().isCreated());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeCreate + 1);
        Resenia testResenia = reseniaList.get(reseniaList.size() - 1);
        assertThat(testResenia.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testResenia.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testResenia.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createReseniaWithExistingId() throws Exception {
        // Create the Resenia with an existing ID
        resenia.setId(1L);
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        int databaseSizeBeforeCreate = reseniaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReseniaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reseniaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResenias() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        // Get all the reseniaList
        restReseniaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resenia.getId().intValue())))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getResenia() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        // Get the resenia
        restReseniaMockMvc
            .perform(get(ENTITY_API_URL_ID, resenia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resenia.getId().intValue()))
            .andExpect(jsonPath("$.calificacion").value(DEFAULT_CALIFICACION))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResenia() throws Exception {
        // Get the resenia
        restReseniaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResenia() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();

        // Update the resenia
        Resenia updatedResenia = reseniaRepository.findById(resenia.getId()).get();
        // Disconnect from session so that the updates on updatedResenia are not directly saved in db
        em.detach(updatedResenia);
        updatedResenia.calificacion(UPDATED_CALIFICACION).comentario(UPDATED_COMENTARIO).fecha(UPDATED_FECHA);
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(updatedResenia);

        restReseniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reseniaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
        Resenia testResenia = reseniaList.get(reseniaList.size() - 1);
        assertThat(testResenia.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testResenia.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testResenia.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reseniaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reseniaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReseniaWithPatch() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();

        // Update the resenia using partial update
        Resenia partialUpdatedResenia = new Resenia();
        partialUpdatedResenia.setId(resenia.getId());

        partialUpdatedResenia.fecha(UPDATED_FECHA);

        restReseniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResenia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResenia))
            )
            .andExpect(status().isOk());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
        Resenia testResenia = reseniaList.get(reseniaList.size() - 1);
        assertThat(testResenia.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testResenia.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testResenia.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateReseniaWithPatch() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();

        // Update the resenia using partial update
        Resenia partialUpdatedResenia = new Resenia();
        partialUpdatedResenia.setId(resenia.getId());

        partialUpdatedResenia.calificacion(UPDATED_CALIFICACION).comentario(UPDATED_COMENTARIO).fecha(UPDATED_FECHA);

        restReseniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResenia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResenia))
            )
            .andExpect(status().isOk());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
        Resenia testResenia = reseniaList.get(reseniaList.size() - 1);
        assertThat(testResenia.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testResenia.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testResenia.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reseniaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResenia() throws Exception {
        int databaseSizeBeforeUpdate = reseniaRepository.findAll().size();
        resenia.setId(count.incrementAndGet());

        // Create the Resenia
        ReseniaDTO reseniaDTO = reseniaMapper.toDto(resenia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReseniaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reseniaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resenia in the database
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResenia() throws Exception {
        // Initialize the database
        reseniaRepository.saveAndFlush(resenia);

        int databaseSizeBeforeDelete = reseniaRepository.findAll().size();

        // Delete the resenia
        restReseniaMockMvc
            .perform(delete(ENTITY_API_URL_ID, resenia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resenia> reseniaList = reseniaRepository.findAll();
        assertThat(reseniaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
