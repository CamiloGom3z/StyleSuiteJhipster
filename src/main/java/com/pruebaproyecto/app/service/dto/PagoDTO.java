package com.pruebaproyecto.app.service.dto;

import com.pruebaproyecto.app.domain.enumeration.MetodoPagoEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Pago} entity.
 */
public class PagoDTO implements Serializable {

    private Long id;

    private BigDecimal monto;

    private Instant fechaPago;

    private MetodoPagoEnum metodoPago;

    private String estado;

    private CitaDTO cita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Instant getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoEnum metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CitaDTO getCita() {
        return cita;
    }

    public void setCita(CitaDTO cita) {
        this.cita = cita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagoDTO)) {
            return false;
        }

        PagoDTO pagoDTO = (PagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoDTO{" +
            "id=" + getId() +
            ", monto=" + getMonto() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cita=" + getCita() +
            "}";
    }
}
