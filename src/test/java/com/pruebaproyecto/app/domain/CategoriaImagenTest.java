package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaImagenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaImagen.class);
        CategoriaImagen categoriaImagen1 = new CategoriaImagen();
        categoriaImagen1.setId(1L);
        CategoriaImagen categoriaImagen2 = new CategoriaImagen();
        categoriaImagen2.setId(categoriaImagen1.getId());
        assertThat(categoriaImagen1).isEqualTo(categoriaImagen2);
        categoriaImagen2.setId(2L);
        assertThat(categoriaImagen1).isNotEqualTo(categoriaImagen2);
        categoriaImagen1.setId(null);
        assertThat(categoriaImagen1).isNotEqualTo(categoriaImagen2);
    }
}
