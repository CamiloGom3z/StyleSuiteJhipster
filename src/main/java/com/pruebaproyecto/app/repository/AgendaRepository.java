package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Agenda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Agenda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {}
