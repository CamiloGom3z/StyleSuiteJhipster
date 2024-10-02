package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Cargo} entity.
 */
public class CargoDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private EmpleadoDTO empleado;

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

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoDTO)) {
            return false;
        }

        CargoDTO cargoDTO = (CargoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cargoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion=" + getDescripcion() +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
