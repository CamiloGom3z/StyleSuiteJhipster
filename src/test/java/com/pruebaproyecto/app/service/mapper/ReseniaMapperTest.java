package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReseniaMapperTest {

    private ReseniaMapper reseniaMapper;

    @BeforeEach
    public void setUp() {
        reseniaMapper = new ReseniaMapperImpl();
    }
}
