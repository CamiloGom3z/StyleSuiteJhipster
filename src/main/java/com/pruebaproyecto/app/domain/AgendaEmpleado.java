package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AgendaEmpleado.
 */
@Entity
@Table(name = "agenda_empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AgendaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_fin")
    private Instant fechaFin;

    @Column(name = "disponible")
    private Boolean disponible;

    @JsonIgnoreProperties(value = { "agendaEmpleado", "servicios", "pagos", "cliente" }, allowSetters = true)
    @OneToOne(mappedBy = "agendaEmpleado")
    private Cita cita;

    @ManyToOne
    @JsonIgnoreProperties(value = { "persona", "cargos", "agendaEmpleados", "establecimiento" }, allowSetters = true)
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgendaEmpleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaInicio() {
        return this.fechaInicio;
    }

    public AgendaEmpleado fechaInicio(Instant fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return this.fechaFin;
    }

    public AgendaEmpleado fechaFin(Instant fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getDisponible() {
        return this.disponible;
    }

    public AgendaEmpleado disponible(Boolean disponible) {
        this.setDisponible(disponible);
        return this;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        if (this.cita != null) {
            this.cita.setAgendaEmpleado(null);
        }
        if (cita != null) {
            cita.setAgendaEmpleado(this);
        }
        this.cita = cita;
    }

    public AgendaEmpleado cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public AgendaEmpleado empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgendaEmpleado)) {
            return false;
        }
        return id != null && id.equals(((AgendaEmpleado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendaEmpleado{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", disponible='" + getDisponible() + "'" +
            "}";
    }
}
