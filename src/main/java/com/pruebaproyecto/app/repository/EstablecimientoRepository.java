package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Establecimiento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Establecimiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {}
