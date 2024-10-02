package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Promocion} entity.
 */
public class PromocionDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private BigDecimal porcentajeDescuento;

    private Instant fechaInicio;

    private Instant fechaFin;

    private String tipoPromocion;

    private Set<ServicioDTO> servicios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
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

    public String getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(String tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    public Set<ServicioDTO> getServicios() {
        return servicios;
    }

    public void setServicios(Set<ServicioDTO> servicios) {
        this.servicios = servicios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromocionDTO)) {
            return false;
        }

        PromocionDTO promocionDTO = (PromocionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promocionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromocionDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", porcentajeDescuento=" + getPorcentajeDescuento() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", tipoPromocion='" + getTipoPromocion() + "'" +
            ", servicios=" + getServicios() +
            "}";
    }
}
