package com.pruebaproyecto.app.web.rest;

import static com.pruebaproyecto.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.TipoServcio;
import com.pruebaproyecto.app.repository.TipoServcioRepository;
import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
import com.pruebaproyecto.app.service.mapper.TipoServcioMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TipoServcioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoServcioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_TIPO_SERVICIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_TIPO_SERVICIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/tipo-servcios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoServcioRepository tipoServcioRepository;

    @Autowired
    private TipoServcioMapper tipoServcioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoServcioMockMvc;

    private TipoServcio tipoServcio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoServcio createEntity(EntityManager em) {
        TipoServcio tipoServcio = new TipoServcio()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .valorTipoServicio(DEFAULT_VALOR_TIPO_SERVICIO);
        return tipoServcio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoServcio createUpdatedEntity(EntityManager em) {
        TipoServcio tipoServcio = new TipoServcio()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .valorTipoServicio(UPDATED_VALOR_TIPO_SERVICIO);
        return tipoServcio;
    }

    @BeforeEach
    public void initTest() {
        tipoServcio = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoServcio() throws Exception {
        int databaseSizeBeforeCreate = tipoServcioRepository.findAll().size();
        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);
        restTipoServcioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeCreate + 1);
        TipoServcio testTipoServcio = tipoServcioList.get(tipoServcioList.size() - 1);
        assertThat(testTipoServcio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoServcio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTipoServcio.getValorTipoServicio()).isEqualByComparingTo(DEFAULT_VALOR_TIPO_SERVICIO);
    }

    @Test
    @Transactional
    void createTipoServcioWithExistingId() throws Exception {
        // Create the TipoServcio with an existing ID
        tipoServcio.setId(1L);
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        int databaseSizeBeforeCreate = tipoServcioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoServcioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTipoServcios() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        // Get all the tipoServcioList
        restTipoServcioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoServcio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valorTipoServicio").value(hasItem(sameNumber(DEFAULT_VALOR_TIPO_SERVICIO))));
    }

    @Test
    @Transactional
    void getTipoServcio() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        // Get the tipoServcio
        restTipoServcioMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoServcio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoServcio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.valorTipoServicio").value(sameNumber(DEFAULT_VALOR_TIPO_SERVICIO)));
    }

    @Test
    @Transactional
    void getNonExistingTipoServcio() throws Exception {
        // Get the tipoServcio
        restTipoServcioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoServcio() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();

        // Update the tipoServcio
        TipoServcio updatedTipoServcio = tipoServcioRepository.findById(tipoServcio.getId()).get();
        // Disconnect from session so that the updates on updatedTipoServcio are not directly saved in db
        em.detach(updatedTipoServcio);
        updatedTipoServcio.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).valorTipoServicio(UPDATED_VALOR_TIPO_SERVICIO);
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(updatedTipoServcio);

        restTipoServcioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoServcioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
        TipoServcio testTipoServcio = tipoServcioList.get(tipoServcioList.size() - 1);
        assertThat(testTipoServcio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoServcio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTipoServcio.getValorTipoServicio()).isEqualByComparingTo(UPDATED_VALOR_TIPO_SERVICIO);
    }

    @Test
    @Transactional
    void putNonExistingTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoServcioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoServcioWithPatch() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();

        // Update the tipoServcio using partial update
        TipoServcio partialUpdatedTipoServcio = new TipoServcio();
        partialUpdatedTipoServcio.setId(tipoServcio.getId());

        partialUpdatedTipoServcio.descripcion(UPDATED_DESCRIPCION).valorTipoServicio(UPDATED_VALOR_TIPO_SERVICIO);

        restTipoServcioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoServcio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoServcio))
            )
            .andExpect(status().isOk());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
        TipoServcio testTipoServcio = tipoServcioList.get(tipoServcioList.size() - 1);
        assertThat(testTipoServcio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoServcio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTipoServcio.getValorTipoServicio()).isEqualByComparingTo(UPDATED_VALOR_TIPO_SERVICIO);
    }

    @Test
    @Transactional
    void fullUpdateTipoServcioWithPatch() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();

        // Update the tipoServcio using partial update
        TipoServcio partialUpdatedTipoServcio = new TipoServcio();
        partialUpdatedTipoServcio.setId(tipoServcio.getId());

        partialUpdatedTipoServcio.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).valorTipoServicio(UPDATED_VALOR_TIPO_SERVICIO);

        restTipoServcioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoServcio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoServcio))
            )
            .andExpect(status().isOk());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
        TipoServcio testTipoServcio = tipoServcioList.get(tipoServcioList.size() - 1);
        assertThat(testTipoServcio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoServcio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTipoServcio.getValorTipoServicio()).isEqualByComparingTo(UPDATED_VALOR_TIPO_SERVICIO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoServcioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoServcio() throws Exception {
        int databaseSizeBeforeUpdate = tipoServcioRepository.findAll().size();
        tipoServcio.setId(count.incrementAndGet());

        // Create the TipoServcio
        TipoServcioDTO tipoServcioDTO = tipoServcioMapper.toDto(tipoServcio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoServcioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoServcioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoServcio in the database
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoServcio() throws Exception {
        // Initialize the database
        tipoServcioRepository.saveAndFlush(tipoServcio);

        int databaseSizeBeforeDelete = tipoServcioRepository.findAll().size();

        // Delete the tipoServcio
        restTipoServcioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoServcio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoServcio> tipoServcioList = tipoServcioRepository.findAll();
        assertThat(tipoServcioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
