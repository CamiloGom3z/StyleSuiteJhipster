package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoServcioMapperTest {

    private TipoServcioMapper tipoServcioMapper;

    @BeforeEach
    public void setUp() {
        tipoServcioMapper = new TipoServcioMapperImpl();
    }
}
