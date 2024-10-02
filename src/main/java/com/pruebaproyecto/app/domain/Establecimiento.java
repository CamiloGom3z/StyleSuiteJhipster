package com.pruebaproyecto.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Establecimiento.
 */
@Entity
@Table(name = "establecimiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Establecimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nit")
    private Long nit;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @OneToMany(mappedBy = "establecimiento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "establecimiento" }, allowSetters = true)
    private Set<Agenda> agendas = new HashSet<>();

    @OneToMany(mappedBy = "establecimiento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "persona", "cargos", "agendaEmpleados", "establecimiento" }, allowSetters = true)
    private Set<Empleado> empleados = new HashSet<>();

    @OneToMany(mappedBy = "establecimiento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "establecimiento" }, allowSetters = true)
    private Set<CategoriaProducto> categoriasProductos = new HashSet<>();

    @OneToMany(mappedBy = "establecimiento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "establecimiento" }, allowSetters = true)
    private Set<CategoriaImagen> categoriasImagens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Establecimiento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Establecimiento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNit() {
        return this.nit;
    }

    public Establecimiento nit(Long nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(Long nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Establecimiento direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Establecimiento telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return this.correoElectronico;
    }

    public Establecimiento correoElectronico(String correoElectronico) {
        this.setCorreoElectronico(correoElectronico);
        return this;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Set<Agenda> getAgendas() {
        return this.agendas;
    }

    public void setAgendas(Set<Agenda> agenda) {
        if (this.agendas != null) {
            this.agendas.forEach(i -> i.setEstablecimiento(null));
        }
        if (agenda != null) {
            agenda.forEach(i -> i.setEstablecimiento(this));
        }
        this.agendas = agenda;
    }

    public Establecimiento agendas(Set<Agenda> agenda) {
        this.setAgendas(agenda);
        return this;
    }

    public Establecimiento addAgendas(Agenda agenda) {
        this.agendas.add(agenda);
        agenda.setEstablecimiento(this);
        return this;
    }

    public Establecimiento removeAgendas(Agenda agenda) {
        this.agendas.remove(agenda);
        agenda.setEstablecimiento(null);
        return this;
    }

    public Set<Empleado> getEmpleados() {
        return this.empleados;
    }

    public void setEmpleados(Set<Empleado> empleados) {
        if (this.empleados != null) {
            this.empleados.forEach(i -> i.setEstablecimiento(null));
        }
        if (empleados != null) {
            empleados.forEach(i -> i.setEstablecimiento(this));
        }
        this.empleados = empleados;
    }

    public Establecimiento empleados(Set<Empleado> empleados) {
        this.setEmpleados(empleados);
        return this;
    }

    public Establecimiento addEmpleados(Empleado empleado) {
        this.empleados.add(empleado);
        empleado.setEstablecimiento(this);
        return this;
    }

    public Establecimiento removeEmpleados(Empleado empleado) {
        this.empleados.remove(empleado);
        empleado.setEstablecimiento(null);
        return this;
    }

    public Set<CategoriaProducto> getCategoriasProductos() {
        return this.categoriasProductos;
    }

    public void setCategoriasProductos(Set<CategoriaProducto> categoriaProductos) {
        if (this.categoriasProductos != null) {
            this.categoriasProductos.forEach(i -> i.setEstablecimiento(null));
        }
        if (categoriaProductos != null) {
            categoriaProductos.forEach(i -> i.setEstablecimiento(this));
        }
        this.categoriasProductos = categoriaProductos;
    }

    public Establecimiento categoriasProductos(Set<CategoriaProducto> categoriaProductos) {
        this.setCategoriasProductos(categoriaProductos);
        return this;
    }

    public Establecimiento addCategoriasProducto(CategoriaProducto categoriaProducto) {
        this.categoriasProductos.add(categoriaProducto);
        categoriaProducto.setEstablecimiento(this);
        return this;
    }

    public Establecimiento removeCategoriasProducto(CategoriaProducto categoriaProducto) {
        this.categoriasProductos.remove(categoriaProducto);
        categoriaProducto.setEstablecimiento(null);
        return this;
    }

    public Set<CategoriaImagen> getCategoriasImagens() {
        return this.categoriasImagens;
    }

    public void setCategoriasImagens(Set<CategoriaImagen> categoriaImagens) {
        if (this.categoriasImagens != null) {
            this.categoriasImagens.forEach(i -> i.setEstablecimiento(null));
        }
        if (categoriaImagens != null) {
            categoriaImagens.forEach(i -> i.setEstablecimiento(this));
        }
        this.categoriasImagens = categoriaImagens;
    }

    public Establecimiento categoriasImagens(Set<CategoriaImagen> categoriaImagens) {
        this.setCategoriasImagens(categoriaImagens);
        return this;
    }

    public Establecimiento addCategoriasImagen(CategoriaImagen categoriaImagen) {
        this.categoriasImagens.add(categoriaImagen);
        categoriaImagen.setEstablecimiento(this);
        return this;
    }

    public Establecimiento removeCategoriasImagen(CategoriaImagen categoriaImagen) {
        this.categoriasImagens.remove(categoriaImagen);
        categoriaImagen.setEstablecimiento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Establecimiento)) {
            return false;
        }
        return id != null && id.equals(((Establecimiento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Establecimiento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nit=" + getNit() +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correoElectronico='" + getCorreoElectronico() + "'" +
            "}";
    }
}
