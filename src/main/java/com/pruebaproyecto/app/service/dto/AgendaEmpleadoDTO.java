package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.AgendaEmpleado} entity.
 */
public class AgendaEmpleadoDTO implements Serializable {

    private Long id;

    private Instant fechaInicio;

    private Instant fechaFin;

    private Boolean disponible;

    private EmpleadoDTO empleado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
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
        if (!(o instanceof AgendaEmpleadoDTO)) {
            return false;
        }

        AgendaEmpleadoDTO agendaEmpleadoDTO = (AgendaEmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agendaEmpleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendaEmpleadoDTO{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", disponible='" + getDisponible() + "'" +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
