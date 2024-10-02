package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Resenia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Resenia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Long> {}
