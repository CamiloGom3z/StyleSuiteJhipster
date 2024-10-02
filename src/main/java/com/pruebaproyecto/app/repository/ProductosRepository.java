package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Productos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Productos entity.
 */
@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {
    default Optional<Productos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Productos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Productos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct productos from Productos productos left join fetch productos.categoriaProducto",
        countQuery = "select count(distinct productos) from Productos productos"
    )
    Page<Productos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct productos from Productos productos left join fetch productos.categoriaProducto")
    List<Productos> findAllWithToOneRelationships();

    @Query("select productos from Productos productos left join fetch productos.categoriaProducto where productos.id =:id")
    Optional<Productos> findOneWithToOneRelationships(@Param("id") Long id);
}
