package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaImagenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaImagenDTO.class);
        CategoriaImagenDTO categoriaImagenDTO1 = new CategoriaImagenDTO();
        categoriaImagenDTO1.setId(1L);
        CategoriaImagenDTO categoriaImagenDTO2 = new CategoriaImagenDTO();
        assertThat(categoriaImagenDTO1).isNotEqualTo(categoriaImagenDTO2);
        categoriaImagenDTO2.setId(categoriaImagenDTO1.getId());
        assertThat(categoriaImagenDTO1).isEqualTo(categoriaImagenDTO2);
        categoriaImagenDTO2.setId(2L);
        assertThat(categoriaImagenDTO1).isNotEqualTo(categoriaImagenDTO2);
        categoriaImagenDTO1.setId(null);
        assertThat(categoriaImagenDTO1).isNotEqualTo(categoriaImagenDTO2);
    }
}
