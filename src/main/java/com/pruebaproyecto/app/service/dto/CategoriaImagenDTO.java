package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.CategoriaImagen} entity.
 */
public class CategoriaImagenDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private EstablecimientoDTO establecimiento;

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

    public EstablecimientoDTO getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(EstablecimientoDTO establecimiento) {
        this.establecimiento = establecimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaImagenDTO)) {
            return false;
        }

        CategoriaImagenDTO categoriaImagenDTO = (CategoriaImagenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaImagenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaImagenDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", establecimiento=" + getEstablecimiento() +
            "}";
    }
}
