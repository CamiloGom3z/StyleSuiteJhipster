package com.pruebaproyecto.app.repository;

import com.pruebaproyecto.app.domain.Promocion;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PromocionRepositoryWithBagRelationshipsImpl implements PromocionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Promocion> fetchBagRelationships(Optional<Promocion> promocion) {
        return promocion.map(this::fetchServicios);
    }

    @Override
    public Page<Promocion> fetchBagRelationships(Page<Promocion> promocions) {
        return new PageImpl<>(fetchBagRelationships(promocions.getContent()), promocions.getPageable(), promocions.getTotalElements());
    }

    @Override
    public List<Promocion> fetchBagRelationships(List<Promocion> promocions) {
        return Optional.of(promocions).map(this::fetchServicios).orElse(Collections.emptyList());
    }

    Promocion fetchServicios(Promocion result) {
        return entityManager
            .createQuery(
                "select promocion from Promocion promocion left join fetch promocion.servicios where promocion is :promocion",
                Promocion.class
            )
            .setParameter("promocion", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Promocion> fetchServicios(List<Promocion> promocions) {
        return entityManager
            .createQuery(
                "select distinct promocion from Promocion promocion left join fetch promocion.servicios where promocion in :promocions",
                Promocion.class
            )
            .setParameter("promocions", promocions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
