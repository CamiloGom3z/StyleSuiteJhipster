package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstablecimientoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstablecimientoDTO.class);
        EstablecimientoDTO establecimientoDTO1 = new EstablecimientoDTO();
        establecimientoDTO1.setId(1L);
        EstablecimientoDTO establecimientoDTO2 = new EstablecimientoDTO();
        assertThat(establecimientoDTO1).isNotEqualTo(establecimientoDTO2);
        establecimientoDTO2.setId(establecimientoDTO1.getId());
        assertThat(establecimientoDTO1).isEqualTo(establecimientoDTO2);
        establecimientoDTO2.setId(2L);
        assertThat(establecimientoDTO1).isNotEqualTo(establecimientoDTO2);
        establecimientoDTO1.setId(null);
        assertThat(establecimientoDTO1).isNotEqualTo(establecimientoDTO2);
    }
}
