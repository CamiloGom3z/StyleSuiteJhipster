package com.pruebaproyecto.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pruebaproyecto.app.domain.Imagen} entity.
 */
public class ImagenDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private String urlImagen;

    private CategoriaImagenDTO categoriaImagen;

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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public CategoriaImagenDTO getCategoriaImagen() {
        return categoriaImagen;
    }

    public void setCategoriaImagen(CategoriaImagenDTO categoriaImagen) {
        this.categoriaImagen = categoriaImagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagenDTO)) {
            return false;
        }

        ImagenDTO imagenDTO = (ImagenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagenDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", urlImagen='" + getUrlImagen() + "'" +
            ", categoriaImagen=" + getCategoriaImagen() +
            "}";
    }
}
