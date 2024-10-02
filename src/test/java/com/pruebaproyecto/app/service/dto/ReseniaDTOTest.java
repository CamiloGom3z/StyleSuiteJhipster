package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReseniaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReseniaDTO.class);
        ReseniaDTO reseniaDTO1 = new ReseniaDTO();
        reseniaDTO1.setId(1L);
        ReseniaDTO reseniaDTO2 = new ReseniaDTO();
        assertThat(reseniaDTO1).isNotEqualTo(reseniaDTO2);
        reseniaDTO2.setId(reseniaDTO1.getId());
        assertThat(reseniaDTO1).isEqualTo(reseniaDTO2);
        reseniaDTO2.setId(2L);
        assertThat(reseniaDTO1).isNotEqualTo(reseniaDTO2);
        reseniaDTO1.setId(null);
        assertThat(reseniaDTO1).isNotEqualTo(reseniaDTO2);
    }
}
