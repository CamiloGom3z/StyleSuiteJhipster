package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoServcioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoServcio.class);
        TipoServcio tipoServcio1 = new TipoServcio();
        tipoServcio1.setId(1L);
        TipoServcio tipoServcio2 = new TipoServcio();
        tipoServcio2.setId(tipoServcio1.getId());
        assertThat(tipoServcio1).isEqualTo(tipoServcio2);
        tipoServcio2.setId(2L);
        assertThat(tipoServcio1).isNotEqualTo(tipoServcio2);
        tipoServcio1.setId(null);
        assertThat(tipoServcio1).isNotEqualTo(tipoServcio2);
    }
}
