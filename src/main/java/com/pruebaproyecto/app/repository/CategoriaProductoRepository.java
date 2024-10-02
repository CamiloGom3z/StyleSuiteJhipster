package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.CategoriaProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CategoriaProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {}
