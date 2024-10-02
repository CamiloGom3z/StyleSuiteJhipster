package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Servicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Servicio entity.
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    default Optional<Servicio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Servicio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Servicio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct servicio from Servicio servicio left join fetch servicio.tipoServicio",
        countQuery = "select count(distinct servicio) from Servicio servicio"
    )
    Page<Servicio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct servicio from Servicio servicio left join fetch servicio.tipoServicio")
    List<Servicio> findAllWithToOneRelationships();

    @Query("select servicio from Servicio servicio left join fetch servicio.tipoServicio where servicio.id =:id")
    Optional<Servicio> findOneWithToOneRelationships(@Param("id") Long id);
}
