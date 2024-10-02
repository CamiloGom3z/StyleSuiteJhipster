package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Imagen.class);
        Imagen imagen1 = new Imagen();
        imagen1.setId(1L);
        Imagen imagen2 = new Imagen();
        imagen2.setId(imagen1.getId());
        assertThat(imagen1).isEqualTo(imagen2);
        imagen2.setId(2L);
        assertThat(imagen1).isNotEqualTo(imagen2);
        imagen1.setId(null);
        assertThat(imagen1).isNotEqualTo(imagen2);
    }
}
