package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cargo_empleado")
    private String cargoEmpleado;

    @Column(name = "salario")
    private Double salario;

    @JsonIgnoreProperties(value = { "citas" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Persona persona;

    @OneToMany(mappedBy = "empleado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "empleado" }, allowSetters = true)
    private Set<Cargo> cargos = new HashSet<>();

    @OneToMany(mappedBy = "empleado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cita", "empleado" }, allowSetters = true)
    private Set<AgendaEmpleado> agendaEmpleados = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "agendas", "empleados", "categoriasProductos", "categoriasImagens" }, allowSetters = true)
    private Establecimiento establecimiento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCargoEmpleado() {
        return this.cargoEmpleado;
    }

    public Empleado cargoEmpleado(String cargoEmpleado) {
        this.setCargoEmpleado(cargoEmpleado);
        return this;
    }

    public void setCargoEmpleado(String cargoEmpleado) {
        this.cargoEmpleado = cargoEmpleado;
    }

    public Double getSalario() {
        return this.salario;
    }

    public Empleado salario(Double salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Empleado persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public Set<Cargo> getCargos() {
        return this.cargos;
    }

    public void setCargos(Set<Cargo> cargos) {
        if (this.cargos != null) {
            this.cargos.forEach(i -> i.setEmpleado(null));
        }
        if (cargos != null) {
            cargos.forEach(i -> i.setEmpleado(this));
        }
        this.cargos = cargos;
    }

    public Empleado cargos(Set<Cargo> cargos) {
        this.setCargos(cargos);
        return this;
    }

    public Empleado addCargo(Cargo cargo) {
        this.cargos.add(cargo);
        cargo.setEmpleado(this);
        return this;
    }

    public Empleado removeCargo(Cargo cargo) {
        this.cargos.remove(cargo);
        cargo.setEmpleado(null);
        return this;
    }

    public Set<AgendaEmpleado> getAgendaEmpleados() {
        return this.agendaEmpleados;
    }

    public void setAgendaEmpleados(Set<AgendaEmpleado> agendaEmpleados) {
        if (this.agendaEmpleados != null) {
            this.agendaEmpleados.forEach(i -> i.setEmpleado(null));
        }
        if (agendaEmpleados != null) {
            agendaEmpleados.forEach(i -> i.setEmpleado(this));
        }
        this.agendaEmpleados = agendaEmpleados;
    }

    public Empleado agendaEmpleados(Set<AgendaEmpleado> agendaEmpleados) {
        this.setAgendaEmpleados(agendaEmpleados);
        return this;
    }

    public Empleado addAgendaEmpleado(AgendaEmpleado agendaEmpleado) {
        this.agendaEmpleados.add(agendaEmpleado);
        agendaEmpleado.setEmpleado(this);
        return this;
    }

    public Empleado removeAgendaEmpleado(AgendaEmpleado agendaEmpleado) {
        this.agendaEmpleados.remove(agendaEmpleado);
        agendaEmpleado.setEmpleado(null);
        return this;
    }

    public Establecimiento getEstablecimiento() {
        return this.establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Empleado establecimiento(Establecimiento establecimiento) {
        this.setEstablecimiento(establecimiento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return id != null && id.equals(((Empleado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", cargoEmpleado='" + getCargoEmpleado() + "'" +
            ", salario=" + getSalario() +
            "}";
    }
}
