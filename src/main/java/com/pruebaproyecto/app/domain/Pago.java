package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pruebaproyecto.app.domain.enumeration.MetodoPagoEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pago.
 */
@Entity
@Table(name = "pago")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "monto", precision = 21, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private Instant fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPagoEnum metodoPago;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "agendaEmpleado", "servicios", "pagos", "cliente" }, allowSetters = true)
    private Cita cita;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public Pago monto(BigDecimal monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Instant getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(Instant fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MetodoPagoEnum getMetodoPago() {
        return this.metodoPago;
    }

    public Pago metodoPago(MetodoPagoEnum metodoPago) {
        this.setMetodoPago(metodoPago);
        return this;
    }

    public void setMetodoPago(MetodoPagoEnum metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return this.estado;
    }

    public Pago estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Pago cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return id != null && id.equals(((Pago) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pago{" +
            "id=" + getId() +
            ", monto=" + getMonto() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
