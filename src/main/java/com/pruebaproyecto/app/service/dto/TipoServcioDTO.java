package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.TipoServcio} entity.
 */
public class TipoServcioDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private BigDecimal valorTipoServicio;

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

    public BigDecimal getValorTipoServicio() {
        return valorTipoServicio;
    }

    public void setValorTipoServicio(BigDecimal valorTipoServicio) {
        this.valorTipoServicio = valorTipoServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoServcioDTO)) {
            return false;
        }

        TipoServcioDTO tipoServcioDTO = (TipoServcioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoServcioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoServcioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", valorTipoServicio=" + getValorTipoServicio() +
            "}";
    }
}
