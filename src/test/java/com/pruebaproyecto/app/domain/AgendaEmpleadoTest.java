package com.pruebaproyecto.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pruebaproyecto.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendaEmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgendaEmpleado.class);
        AgendaEmpleado agendaEmpleado1 = new AgendaEmpleado();
        agendaEmpleado1.setId(1L);
        AgendaEmpleado agendaEmpleado2 = new AgendaEmpleado();
        agendaEmpleado2.setId(agendaEmpleado1.getId());
        assertThat(agendaEmpleado1).isEqualTo(agendaEmpleado2);
        agendaEmpleado2.setId(2L);
        assertThat(agendaEmpleado1).isNotEqualTo(agendaEmpleado2);
        agendaEmpleado1.setId(null);
        assertThat(agendaEmpleado1).isNotEqualTo(agendaEmpleado2);
    }
}
