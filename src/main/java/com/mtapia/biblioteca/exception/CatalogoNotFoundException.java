package com.mtapia.biblioteca.exception;

// -------------------- IMPORTACIONES -------------------------
// Importa los códigos de estado HTTP que usaremos para asignar un 404
import org.springframework.http.HttpStatus;

// Permite asociar la excepción a un código de estado HTTP automáticamente
import org.springframework.web.bind.annotation.ResponseStatus;

// -------------------- CLASE -------------------------

/**
 * Excepción personalizada.
 * Esta clase se lanza cuando un usuario intenta buscar una película por ID
 * y esta no existe en la lista.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Hace que, al lanzarla, Spring devuelva directamente un 404 Not Found
public class CatalogoNotFoundException extends RuntimeException {
    
    /**
     * Constructor de la excepción.
     * Recibe el ID que el usuario buscó y no fue encontrado.
     * 
     * @param id ID de libro que no existe.
     */
    public CatalogoNotFoundException(Long id) {
        // Generamos un mensaje personalizado que luego será visible en la respuesta de
        // error
        super("Catalogo con id " + id + " no fue encontrado");
    }

}
