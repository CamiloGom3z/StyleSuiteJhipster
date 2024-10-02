package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Empleado} entity.
 */
public class EmpleadoDTO implements Serializable {

    private Long id;

    private String cargoEmpleado;

    private Double salario;

    private PersonaDTO persona;

    private EstablecimientoDTO establecimiento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargoEmpleado() {
        return cargoEmpleado;
    }

    public void setCargoEmpleado(String cargoEmpleado) {
        this.cargoEmpleado = cargoEmpleado;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
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
        if (!(o instanceof EmpleadoDTO)) {
            return false;
        }

        EmpleadoDTO empleadoDTO = (EmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDTO{" +
            "id=" + getId() +
            ", cargoEmpleado='" + getCargoEmpleado() + "'" +
            ", salario=" + getSalario() +
            ", persona=" + getPersona() +
            ", establecimiento=" + getEstablecimiento() +
            "}";
    }
}
