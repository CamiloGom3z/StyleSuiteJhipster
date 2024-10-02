package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagenMapperTest {

    private ImagenMapper imagenMapper;

    @BeforeEach
    public void setUp() {
        imagenMapper = new ImagenMapperImpl();
    }
}
