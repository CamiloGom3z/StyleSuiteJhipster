package com.pruebaproyecto.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendaEmpleadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgendaEmpleadoDTO.class);
        AgendaEmpleadoDTO agendaEmpleadoDTO1 = new AgendaEmpleadoDTO();
        agendaEmpleadoDTO1.setId(1L);
        AgendaEmpleadoDTO agendaEmpleadoDTO2 = new AgendaEmpleadoDTO();
        assertThat(agendaEmpleadoDTO1).isNotEqualTo(agendaEmpleadoDTO2);
        agendaEmpleadoDTO2.setId(agendaEmpleadoDTO1.getId());
        assertThat(agendaEmpleadoDTO1).isEqualTo(agendaEmpleadoDTO2);
        agendaEmpleadoDTO2.setId(2L);
        assertThat(agendaEmpleadoDTO1).isNotEqualTo(agendaEmpleadoDTO2);
        agendaEmpleadoDTO1.setId(null);
        assertThat(agendaEmpleadoDTO1).isNotEqualTo(agendaEmpleadoDTO2);
    }
}
