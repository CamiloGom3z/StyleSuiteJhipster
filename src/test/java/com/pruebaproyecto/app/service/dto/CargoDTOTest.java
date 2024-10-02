package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoDTO.class);
        CargoDTO cargoDTO1 = new CargoDTO();
        cargoDTO1.setId(1L);
        CargoDTO cargoDTO2 = new CargoDTO();
        assertThat(cargoDTO1).isNotEqualTo(cargoDTO2);
        cargoDTO2.setId(cargoDTO1.getId());
        assertThat(cargoDTO1).isEqualTo(cargoDTO2);
        cargoDTO2.setId(2L);
        assertThat(cargoDTO1).isNotEqualTo(cargoDTO2);
        cargoDTO1.setId(null);
        assertThat(cargoDTO1).isNotEqualTo(cargoDTO2);
    }
}
