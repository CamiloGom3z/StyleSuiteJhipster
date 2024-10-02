package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Servicio} entity.
 */
public class ServicioDTO implements Serializable {

    private Long id;

    private BigDecimal valorTotalServicio;

    private String descripcion;

    private Instant fechaInicio;

    private Instant fechaFin;

    private TipoServcioDTO tipoServicio;

    private CitaDTO cita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTotalServicio() {
        return valorTotalServicio;
    }

    public void setValorTotalServicio(BigDecimal valorTotalServicio) {
        this.valorTotalServicio = valorTotalServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Instant fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Instant fechaFin) {
        this.fechaFin = fechaFin;
    }

    public TipoServcioDTO getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServcioDTO tipoServicio) {
        this.tipoServicio = tipoServicio;
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
        if (!(o instanceof ServicioDTO)) {
            return false;
        }

        ServicioDTO servicioDTO = (ServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDTO{" +
            "id=" + getId() +
            ", valorTotalServicio=" + getValorTotalServicio() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", tipoServicio=" + getTipoServicio() +
            ", cita=" + getCita() +
            "}";
    }
}
