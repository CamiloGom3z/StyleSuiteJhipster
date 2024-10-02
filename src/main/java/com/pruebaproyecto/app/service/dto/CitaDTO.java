package com.pruebaproyecto.app.service.dto;

import com.pruebaproyecto.app.domain.enumeration.EstadoCitaEnum;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Cita} entity.
 */
public class CitaDTO implements Serializable {

    private Long id;

    private Instant fechaCita;

    private Instant fechaFinCita;

    private EstadoCitaEnum estado;

    private String notas;

    private AgendaEmpleadoDTO agendaEmpleado;

    private PersonaDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Instant fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Instant getFechaFinCita() {
        return fechaFinCita;
    }

    public void setFechaFinCita(Instant fechaFinCita) {
        this.fechaFinCita = fechaFinCita;
    }

    public EstadoCitaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoCitaEnum estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public AgendaEmpleadoDTO getAgendaEmpleado() {
        return agendaEmpleado;
    }

    public void setAgendaEmpleado(AgendaEmpleadoDTO agendaEmpleado) {
        this.agendaEmpleado = agendaEmpleado;
    }

    public PersonaDTO getCliente() {
        return cliente;
    }

    public void setCliente(PersonaDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaDTO)) {
            return false;
        }

        CitaDTO citaDTO = (CitaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaDTO{" +
            "id=" + getId() +
            ", fechaCita='" + getFechaCita() + "'" +
            ", fechaFinCita='" + getFechaFinCita() + "'" +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            ", agendaEmpleado=" + getAgendaEmpleado() +
            ", cliente=" + getCliente() +
            "}";
    }
}
