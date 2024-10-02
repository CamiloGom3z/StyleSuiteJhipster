package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgendaMapperTest {

    private AgendaMapper agendaMapper;

    @BeforeEach
    public void setUp() {
        agendaMapper = new AgendaMapperImpl();
    }
}
