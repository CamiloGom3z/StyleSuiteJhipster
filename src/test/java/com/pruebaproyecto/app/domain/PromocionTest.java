package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PromocionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promocion.class);
        Promocion promocion1 = new Promocion();
        promocion1.setId(1L);
        Promocion promocion2 = new Promocion();
        promocion2.setId(promocion1.getId());
        assertThat(promocion1).isEqualTo(promocion2);
        promocion2.setId(2L);
        assertThat(promocion1).isNotEqualTo(promocion2);
        promocion1.setId(null);
        assertThat(promocion1).isNotEqualTo(promocion2);
    }
}
