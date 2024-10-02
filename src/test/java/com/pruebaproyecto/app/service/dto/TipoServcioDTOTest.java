package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoServcioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoServcioDTO.class);
        TipoServcioDTO tipoServcioDTO1 = new TipoServcioDTO();
        tipoServcioDTO1.setId(1L);
        TipoServcioDTO tipoServcioDTO2 = new TipoServcioDTO();
        assertThat(tipoServcioDTO1).isNotEqualTo(tipoServcioDTO2);
        tipoServcioDTO2.setId(tipoServcioDTO1.getId());
        assertThat(tipoServcioDTO1).isEqualTo(tipoServcioDTO2);
        tipoServcioDTO2.setId(2L);
        assertThat(tipoServcioDTO1).isNotEqualTo(tipoServcioDTO2);
        tipoServcioDTO1.setId(null);
        assertThat(tipoServcioDTO1).isNotEqualTo(tipoServcioDTO2);
    }
}
