package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReseniaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resenia.class);
        Resenia resenia1 = new Resenia();
        resenia1.setId(1L);
        Resenia resenia2 = new Resenia();
        resenia2.setId(resenia1.getId());
        assertThat(resenia1).isEqualTo(resenia2);
        resenia2.setId(2L);
        assertThat(resenia1).isNotEqualTo(resenia2);
        resenia1.setId(null);
        assertThat(resenia1).isNotEqualTo(resenia2);
    }
}
