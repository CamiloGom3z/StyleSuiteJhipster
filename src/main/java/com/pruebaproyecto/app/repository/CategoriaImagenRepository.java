package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.CategoriaImagen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CategoriaImagen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaImagenRepository extends JpaRepository<CategoriaImagen, Long> {}
