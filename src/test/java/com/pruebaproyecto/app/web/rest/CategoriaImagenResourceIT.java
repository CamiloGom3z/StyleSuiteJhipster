package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.CategoriaImagen;
import com.pruebaproyecto.app.repository.CategoriaImagenRepository;
import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
import com.pruebaproyecto.app.service.mapper.CategoriaImagenMapper;
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
 * Integration tests for the {@link CategoriaImagenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaImagenResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categoria-imagens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaImagenRepository categoriaImagenRepository;

    @Autowired
    private CategoriaImagenMapper categoriaImagenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaImagenMockMvc;

    private CategoriaImagen categoriaImagen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaImagen createEntity(EntityManager em) {
        CategoriaImagen categoriaImagen = new CategoriaImagen().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return categoriaImagen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaImagen createUpdatedEntity(EntityManager em) {
        CategoriaImagen categoriaImagen = new CategoriaImagen().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return categoriaImagen;
    }

    @BeforeEach
    public void initTest() {
        categoriaImagen = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaImagen() throws Exception {
        int databaseSizeBeforeCreate = categoriaImagenRepository.findAll().size();
        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);
        restCategoriaImagenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaImagen testCategoriaImagen = categoriaImagenList.get(categoriaImagenList.size() - 1);
        assertThat(testCategoriaImagen.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCategoriaImagen.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCategoriaImagenWithExistingId() throws Exception {
        // Create the CategoriaImagen with an existing ID
        categoriaImagen.setId(1L);
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        int databaseSizeBeforeCreate = categoriaImagenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaImagenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategoriaImagens() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        // Get all the categoriaImagenList
        restCategoriaImagenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaImagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCategoriaImagen() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        // Get the categoriaImagen
        restCategoriaImagenMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaImagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaImagen.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaImagen() throws Exception {
        // Get the categoriaImagen
        restCategoriaImagenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategoriaImagen() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();

        // Update the categoriaImagen
        CategoriaImagen updatedCategoriaImagen = categoriaImagenRepository.findById(categoriaImagen.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaImagen are not directly saved in db
        em.detach(updatedCategoriaImagen);
        updatedCategoriaImagen.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(updatedCategoriaImagen);

        restCategoriaImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaImagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
        CategoriaImagen testCategoriaImagen = categoriaImagenList.get(categoriaImagenList.size() - 1);
        assertThat(testCategoriaImagen.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCategoriaImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaImagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaImagenWithPatch() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();

        // Update the categoriaImagen using partial update
        CategoriaImagen partialUpdatedCategoriaImagen = new CategoriaImagen();
        partialUpdatedCategoriaImagen.setId(categoriaImagen.getId());

        partialUpdatedCategoriaImagen.descripcion(UPDATED_DESCRIPCION);

        restCategoriaImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaImagen))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
        CategoriaImagen testCategoriaImagen = categoriaImagenList.get(categoriaImagenList.size() - 1);
        assertThat(testCategoriaImagen.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCategoriaImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaImagenWithPatch() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();

        // Update the categoriaImagen using partial update
        CategoriaImagen partialUpdatedCategoriaImagen = new CategoriaImagen();
        partialUpdatedCategoriaImagen.setId(categoriaImagen.getId());

        partialUpdatedCategoriaImagen.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restCategoriaImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaImagen))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
        CategoriaImagen testCategoriaImagen = categoriaImagenList.get(categoriaImagenList.size() - 1);
        assertThat(testCategoriaImagen.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCategoriaImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaImagenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaImagen() throws Exception {
        int databaseSizeBeforeUpdate = categoriaImagenRepository.findAll().size();
        categoriaImagen.setId(count.incrementAndGet());

        // Create the CategoriaImagen
        CategoriaImagenDTO categoriaImagenDTO = categoriaImagenMapper.toDto(categoriaImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaImagenMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaImagenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaImagen in the database
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaImagen() throws Exception {
        // Initialize the database
        categoriaImagenRepository.saveAndFlush(categoriaImagen);

        int databaseSizeBeforeDelete = categoriaImagenRepository.findAll().size();

        // Delete the categoriaImagen
        restCategoriaImagenMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaImagen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaImagen> categoriaImagenList = categoriaImagenRepository.findAll();
        assertThat(categoriaImagenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
