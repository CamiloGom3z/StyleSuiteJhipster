package com.pruebaproyecto.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Imagen;
import com.pruebaproyecto.app.repository.ImagenRepository;
import com.pruebaproyecto.app.service.ImagenService;
import com.pruebaproyecto.app.service.dto.ImagenDTO;
import com.pruebaproyecto.app.service.mapper.ImagenMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImagenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImagenResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_URL_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_URL_IMAGEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/imagens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagenRepository imagenRepository;

    @Mock
    private ImagenRepository imagenRepositoryMock;

    @Autowired
    private ImagenMapper imagenMapper;

    @Mock
    private ImagenService imagenServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagenMockMvc;

    private Imagen imagen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imagen createEntity(EntityManager em) {
        Imagen imagen = new Imagen().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).urlImagen(DEFAULT_URL_IMAGEN);
        return imagen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imagen createUpdatedEntity(EntityManager em) {
        Imagen imagen = new Imagen().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).urlImagen(UPDATED_URL_IMAGEN);
        return imagen;
    }

    @BeforeEach
    public void initTest() {
        imagen = createEntity(em);
    }

    @Test
    @Transactional
    void createImagen() throws Exception {
        int databaseSizeBeforeCreate = imagenRepository.findAll().size();
        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);
        restImagenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagenDTO)))
            .andExpect(status().isCreated());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeCreate + 1);
        Imagen testImagen = imagenList.get(imagenList.size() - 1);
        assertThat(testImagen.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testImagen.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testImagen.getUrlImagen()).isEqualTo(DEFAULT_URL_IMAGEN);
    }

    @Test
    @Transactional
    void createImagenWithExistingId() throws Exception {
        // Create the Imagen with an existing ID
        imagen.setId(1L);
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        int databaseSizeBeforeCreate = imagenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImagens() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        // Get all the imagenList
        restImagenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].urlImagen").value(hasItem(DEFAULT_URL_IMAGEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImagensWithEagerRelationshipsIsEnabled() throws Exception {
        when(imagenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImagenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imagenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImagensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(imagenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImagenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imagenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        // Get the imagen
        restImagenMockMvc
            .perform(get(ENTITY_API_URL_ID, imagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imagen.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.urlImagen").value(DEFAULT_URL_IMAGEN));
    }

    @Test
    @Transactional
    void getNonExistingImagen() throws Exception {
        // Get the imagen
        restImagenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();

        // Update the imagen
        Imagen updatedImagen = imagenRepository.findById(imagen.getId()).get();
        // Disconnect from session so that the updates on updatedImagen are not directly saved in db
        em.detach(updatedImagen);
        updatedImagen.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).urlImagen(UPDATED_URL_IMAGEN);
        ImagenDTO imagenDTO = imagenMapper.toDto(updatedImagen);

        restImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
        Imagen testImagen = imagenList.get(imagenList.size() - 1);
        assertThat(testImagen.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testImagen.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
    }

    @Test
    @Transactional
    void putNonExistingImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagenWithPatch() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();

        // Update the imagen using partial update
        Imagen partialUpdatedImagen = new Imagen();
        partialUpdatedImagen.setId(imagen.getId());

        partialUpdatedImagen.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).urlImagen(UPDATED_URL_IMAGEN);

        restImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagen))
            )
            .andExpect(status().isOk());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
        Imagen testImagen = imagenList.get(imagenList.size() - 1);
        assertThat(testImagen.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testImagen.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
    }

    @Test
    @Transactional
    void fullUpdateImagenWithPatch() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();

        // Update the imagen using partial update
        Imagen partialUpdatedImagen = new Imagen();
        partialUpdatedImagen.setId(imagen.getId());

        partialUpdatedImagen.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).urlImagen(UPDATED_URL_IMAGEN);

        restImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagen))
            )
            .andExpect(status().isOk());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
        Imagen testImagen = imagenList.get(imagenList.size() - 1);
        assertThat(testImagen.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testImagen.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testImagen.getUrlImagen()).isEqualTo(UPDATED_URL_IMAGEN);
    }

    @Test
    @Transactional
    void patchNonExistingImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImagen() throws Exception {
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();
        imagen.setId(count.incrementAndGet());

        // Create the Imagen
        ImagenDTO imagenDTO = imagenMapper.toDto(imagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imagenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Imagen in the database
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        int databaseSizeBeforeDelete = imagenRepository.findAll().size();

        // Delete the imagen
        restImagenMockMvc
            .perform(delete(ENTITY_API_URL_ID, imagen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Imagen> imagenList = imagenRepository.findAll();
        assertThat(imagenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
