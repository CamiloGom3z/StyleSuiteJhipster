package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstablecimientoMapperTest {

    private EstablecimientoMapper establecimientoMapper;

    @BeforeEach
    public void setUp() {
        establecimientoMapper = new EstablecimientoMapperImpl();
    }
}
