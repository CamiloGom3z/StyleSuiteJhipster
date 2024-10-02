package com.pruebaproyecto.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CargoMapperTest {

    private CargoMapper cargoMapper;

    @BeforeEach
    public void setUp() {
        cargoMapper = new CargoMapperImpl();
    }
}
