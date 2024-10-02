package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaImagenMapperTest {

    private CategoriaImagenMapper categoriaImagenMapper;

    @BeforeEach
    public void setUp() {
        categoriaImagenMapper = new CategoriaImagenMapperImpl();
    }
}
