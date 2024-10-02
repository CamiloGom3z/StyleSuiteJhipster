package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstablecimientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Establecimiento.class);
        Establecimiento establecimiento1 = new Establecimiento();
        establecimiento1.setId(1L);
        Establecimiento establecimiento2 = new Establecimiento();
        establecimiento2.setId(establecimiento1.getId());
        assertThat(establecimiento1).isEqualTo(establecimiento2);
        establecimiento2.setId(2L);
        assertThat(establecimiento1).isNotEqualTo(establecimiento2);
        establecimiento1.setId(null);
        assertThat(establecimiento1).isNotEqualTo(establecimiento2);
    }
}
