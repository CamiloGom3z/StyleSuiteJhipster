package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor_total_servicio", precision = 21, scale = 2)
    private BigDecimal valorTotalServicio;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @OneToMany(mappedBy = "servicio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicio" }, allowSetters = true)
    private Set<Resenia> resenias = new HashSet<>();

    @ManyToOne
    private TipoServcio tipoServicio;

    @ManyToOne
    @JsonIgnoreProperties(value = { "agendaEmpleado", "servicios", "pagos", "cliente" }, allowSetters = true)
    private Cita cita;

    @ManyToMany(mappedBy = "servicios")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicios" }, allowSetters = true)
    private Set<Promocion> promociones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Servicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTotalServicio() {
        return this.valorTotalServicio;
    }

    public Servicio valorTotalServicio(BigDecimal valorTotalServicio) {
        this.setValorTotalServicio(valorTotalServicio);
        return this;
    }

    public void setValorTotalServicio(BigDecimal valorTotalServicio) {
        this.valorTotalServicio = valorTotalServicio;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Servicio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public Servicio fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public Servicio fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Set<Resenia> getResenias() {
        return this.resenias;
    }

    public void setResenias(Set<Resenia> resenias) {
        if (this.resenias != null) {
            this.resenias.forEach(i -> i.setServicio(null));
        }
        if (resenias != null) {
            resenias.forEach(i -> i.setServicio(this));
        }
        this.resenias = resenias;
    }

    public Servicio resenias(Set<Resenia> resenias) {
        this.setResenias(resenias);
        return this;
    }

    public Servicio addResenias(Resenia resenia) {
        this.resenias.add(resenia);
        resenia.setServicio(this);
        return this;
    }

    public Servicio removeResenias(Resenia resenia) {
        this.resenias.remove(resenia);
        resenia.setServicio(null);
        return this;
    }

    public TipoServcio getTipoServicio() {
        return this.tipoServicio;
    }

    public void setTipoServicio(TipoServcio tipoServcio) {
        this.tipoServicio = tipoServcio;
    }

    public Servicio tipoServicio(TipoServcio tipoServcio) {
        this.setTipoServicio(tipoServcio);
        return this;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Servicio cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    public Set<Promocion> getPromociones() {
        return this.promociones;
    }

    public void setPromociones(Set<Promocion> promocions) {
        if (this.promociones != null) {
            this.promociones.forEach(i -> i.removeServicios(this));
        }
        if (promocions != null) {
            promocions.forEach(i -> i.addServicios(this));
        }
        this.promociones = promocions;
    }

    public Servicio promociones(Set<Promocion> promocions) {
        this.setPromociones(promocions);
        return this;
    }

    public Servicio addPromociones(Promocion promocion) {
        this.promociones.add(promocion);
        promocion.getServicios().add(this);
        return this;
    }

    public Servicio removePromociones(Promocion promocion) {
        this.promociones.remove(promocion);
        promocion.getServicios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return id != null && id.equals(((Servicio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Servicio{" +
            "id=" + getId() +
            ", valorTotalServicio=" + getValorTotalServicio() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
