package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.AgendaEmpleado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AgendaEmpleado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgendaEmpleadoRepository extends JpaRepository<AgendaEmpleado, Long> {}
