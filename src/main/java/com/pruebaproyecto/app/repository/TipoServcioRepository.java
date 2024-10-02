package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.TipoServcio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoServcio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoServcioRepository extends JpaRepository<TipoServcio, Long> {}
