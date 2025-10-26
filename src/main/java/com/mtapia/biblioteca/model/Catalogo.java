package com.mtapia.biblioteca.model;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Clase que representa a una Película.
 * Es el "molde" de cada objeto de tipo Película que manejará nuestro
 * microservicio.
 */
@Data // Lombok: genera automáticamente todos los getters, setters, toString(),
      // equals(), hashCode()
@AllArgsConstructor // Lombok: genera un constructor con todos los atributos como parámetros
@NoArgsConstructor // Lombok: genera un constructor sin parámetros (vacío)
@Entity // 🔵 Indicamos que es una entidad de base de datos
@Table(name = "FS_CATALOGOS")
@JsonPropertyOrder({"id", "titulo","autor","anio_publicacion","genero"}) // orden de json
public class Catalogo {

    // identificador 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    // titulo del libro
    @NotBlank(message = "Título no puede estar vacío")
    @Size(min = 1, max = 200, message = "Título debe tener entre 1 y 200 caracteres")
    @Column(name = "TITULO", length = 255, nullable = false)
    private String titulo;
    
    // autor del libro
    @NotBlank(message = "Autor no puede estar vacío")
    @Size(min = 1, max = 200, message = "Autor debe tener entre 1 y 200 caracteres")
    @Column(name = "AUTOR", length = 200, nullable = false)
    private String autor;
    
    // año de publicación
    @NotNull(message = "Año de publicación no puede estar vacío")
    @Min(value = 1000, message = "Año debe ser mayor a 1000")
    @Max(value = 2999, message = "Año no puede ser en el futuro")
    @Column(name = "ANIO_PUBLICACION")
    private Integer anioPublicacion;
    
    // genero de la publicación
    @NotBlank(message = "Género no puede estar vacío")
    @Size(min = 1, max = 75, message = "Género debe tener entre 1 y 75 caracteres")
    @Column(name = "GENERO", length = 75, nullable = false)
    private String genero;

}
