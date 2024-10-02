package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Resenia} entity.
 */
public class ReseniaDTO implements Serializable {

    private Long id;

    private Integer calificacion;

    private String comentario;

    private Instant fecha;

    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReseniaDTO)) {
            return false;
        }

        ReseniaDTO reseniaDTO = (ReseniaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reseniaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReseniaDTO{" +
            "id=" + getId() +
            ", calificacion=" + getCalificacion() +
            ", comentario='" + getComentario() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", servicio=" + getServicio() +
            "}";
    }
}
