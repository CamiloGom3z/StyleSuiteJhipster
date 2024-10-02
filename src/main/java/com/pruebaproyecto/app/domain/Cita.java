package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pruebaproyecto.app.domain.enumeration.EstadoCitaEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_cita")
    private Instant fechaCita;

    @Column(name = "fecha_fin_cita")
    private Instant fechaFinCita;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCitaEnum estado;

    @Column(name = "notas")
    private String notas;

    @JsonIgnoreProperties(value = { "cita", "empleado" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AgendaEmpleado agendaEmpleado;

    @OneToMany(mappedBy = "cita")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resenias", "tipoServicio", "cita", "promociones" }, allowSetters = true)
    private Set<Servicio> servicios = new HashSet<>();

    @OneToMany(mappedBy = "cita")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cita" }, allowSetters = true)
    private Set<Pago> pagos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    private Persona cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCita() {
        return this.fechaCita;
    }

    public Cita fechaCita(Instant fechaCita) {
        this.setFechaCita(fechaCita);
        return this;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Instant getFechaFinCita() {
        return this.fechaFinCita;
    }

    public Cita fechaFinCita(Instant fechaFinCita) {
        this.setFechaFinCita(fechaFinCita);
        return this;
    }

    public void setFechaFinCita(Instant fechaFinCita) {
        this.fechaFinCita = fechaFinCita;
    }

    public EstadoCitaEnum getEstado() {
        return this.estado;
    }

    public Cita estado(EstadoCitaEnum estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoCitaEnum estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return this.notas;
    }

    public Cita notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public AgendaEmpleado getAgendaEmpleado() {
        return this.agendaEmpleado;
    }

    public void setAgendaEmpleado(AgendaEmpleado agendaEmpleado) {
        this.agendaEmpleado = agendaEmpleado;
    }

    public Cita agendaEmpleado(AgendaEmpleado agendaEmpleado) {
        this.setAgendaEmpleado(agendaEmpleado);
        return this;
    }

    public Set<Servicio> getServicios() {
        return this.servicios;
    }

    public void setServicios(Set<Servicio> servicios) {
        if (this.servicios != null) {
            this.servicios.forEach(i -> i.setCita(null));
        }
        if (servicios != null) {
            servicios.forEach(i -> i.setCita(this));
        }
        this.servicios = servicios;
    }

    public Cita servicios(Set<Servicio> servicios) {
        this.setServicios(servicios);
        return this;
    }

    public Cita addServicios(Servicio servicio) {
        this.servicios.add(servicio);
        servicio.setCita(this);
        return this;
    }

    public Cita removeServicios(Servicio servicio) {
        this.servicios.remove(servicio);
        servicio.setCita(null);
        return this;
    }

    public Set<Pago> getPagos() {
        return this.pagos;
    }

    public void setPagos(Set<Pago> pagos) {
        if (this.pagos != null) {
            this.pagos.forEach(i -> i.setCita(null));
        }
        if (pagos != null) {
            pagos.forEach(i -> i.setCita(this));
        }
        this.pagos = pagos;
    }

    public Cita pagos(Set<Pago> pagos) {
        this.setPagos(pagos);
        return this;
    }

    public Cita addPagos(Pago pago) {
        this.pagos.add(pago);
        pago.setCita(this);
        return this;
    }

    public Cita removePagos(Pago pago) {
        this.pagos.remove(pago);
        pago.setCita(null);
        return this;
    }

    public Persona getCliente() {
        return this.cliente;
    }

    public void setCliente(Persona persona) {
        this.cliente = persona;
    }

    public Cita cliente(Persona persona) {
        this.setCliente(persona);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return id != null && id.equals(((Cita) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", fechaCita='" + getFechaCita() + "'" +
            ", fechaFinCita='" + getFechaFinCita() + "'" +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
