package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgendaEmpleadoMapperTest {

    private AgendaEmpleadoMapper agendaEmpleadoMapper;

    @BeforeEach
    public void setUp() {
        agendaEmpleadoMapper = new AgendaEmpleadoMapperImpl();
    }
}
