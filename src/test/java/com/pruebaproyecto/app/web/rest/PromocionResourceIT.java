package com.pruebaproyecto.app.web.rest;

import static com.pruebaproyecto.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pruebaproyecto.app.IntegrationTest;
import com.pruebaproyecto.app.domain.Promocion;
import com.pruebaproyecto.app.repository.PromocionRepository;
import com.pruebaproyecto.app.service.PromocionService;
import com.pruebaproyecto.app.service.dto.PromocionDTO;
import com.pruebaproyecto.app.service.mapper.PromocionMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PromocionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PromocionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PORCENTAJE_DESCUENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PORCENTAJE_DESCUENTO = new BigDecimal(2);

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TIPO_PROMOCION = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_PROMOCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/promocions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromocionRepository promocionRepository;

    @Mock
    private PromocionRepository promocionRepositoryMock;

    @Autowired
    private PromocionMapper promocionMapper;

    @Mock
    private PromocionService promocionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromocionMockMvc;

    private Promocion promocion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createEntity(EntityManager em) {
        Promocion promocion = new Promocion()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .porcentajeDescuento(DEFAULT_PORCENTAJE_DESCUENTO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .tipoPromocion(DEFAULT_TIPO_PROMOCION);
        return promocion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createUpdatedEntity(EntityManager em) {
        Promocion promocion = new Promocion()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .tipoPromocion(UPDATED_TIPO_PROMOCION);
        return promocion;
    }

    @BeforeEach
    public void initTest() {
        promocion = createEntity(em);
    }

    @Test
    @Transactional
    void createPromocion() throws Exception {
        int databaseSizeBeforeCreate = promocionRepository.findAll().size();
        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);
        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocionDTO)))
            .andExpect(status().isCreated());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate + 1);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualByComparingTo(DEFAULT_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getTipoPromocion()).isEqualTo(DEFAULT_TIPO_PROMOCION);
    }

    @Test
    @Transactional
    void createPromocionWithExistingId() throws Exception {
        // Create the Promocion with an existing ID
        promocion.setId(1L);
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        int databaseSizeBeforeCreate = promocionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromocionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPromocions() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        // Get all the promocionList
        restPromocionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promocion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].porcentajeDescuento").value(hasItem(sameNumber(DEFAULT_PORCENTAJE_DESCUENTO))))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].tipoPromocion").value(hasItem(DEFAULT_TIPO_PROMOCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromocionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promocionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPromocionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(promocionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        // Get the promocion
        restPromocionMockMvc
            .perform(get(ENTITY_API_URL_ID, promocion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promocion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.porcentajeDescuento").value(sameNumber(DEFAULT_PORCENTAJE_DESCUENTO)))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.tipoPromocion").value(DEFAULT_TIPO_PROMOCION));
    }

    @Test
    @Transactional
    void getNonExistingPromocion() throws Exception {
        // Get the promocion
        restPromocionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion
        Promocion updatedPromocion = promocionRepository.findById(promocion.getId()).get();
        // Disconnect from session so that the updates on updatedPromocion are not directly saved in db
        em.detach(updatedPromocion);
        updatedPromocion
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .tipoPromocion(UPDATED_TIPO_PROMOCION);
        PromocionDTO promocionDTO = promocionMapper.toDto(updatedPromocion);

        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promocionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualByComparingTo(UPDATED_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getTipoPromocion()).isEqualTo(UPDATED_TIPO_PROMOCION);
    }

    @Test
    @Transactional
    void putNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promocionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promocionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion.nombre(UPDATED_NOMBRE).tipoPromocion(UPDATED_TIPO_PROMOCION);

        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualByComparingTo(DEFAULT_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getTipoPromocion()).isEqualTo(UPDATED_TIPO_PROMOCION);
    }

    @Test
    @Transactional
    void fullUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .tipoPromocion(UPDATED_TIPO_PROMOCION);

        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            )
            .andExpect(status().isOk());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualByComparingTo(UPDATED_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getTipoPromocion()).isEqualTo(UPDATED_TIPO_PROMOCION);
    }

    @Test
    @Transactional
    void patchNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promocionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().size();
        promocion.setId(count.incrementAndGet());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromocionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(promocionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromocion() throws Exception {
        // Initialize the database
        promocionRepository.saveAndFlush(promocion);

        int databaseSizeBeforeDelete = promocionRepository.findAll().size();

        // Delete the promocion
        restPromocionMockMvc
            .perform(delete(ENTITY_API_URL_ID, promocion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Promocion> promocionList = promocionRepository.findAll();
        assertThat(promocionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
