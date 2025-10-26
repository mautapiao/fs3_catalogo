package com.mtapia.biblioteca.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que expone la página de inicio o "home" de la API.
 * Aquí proporciono información básica sobre la API y los endpoints disponibles.
 */
@RestController
public class HomeController {

    /**
     * Método que responde a la ruta raíz "/" mediante GET.
     * Construyo un mapa con información relevante de la API, como:
     * - título de la aplicación
     * - mensaje o descripción
     * - rutas disponibles para consultar todos los catálogos o uno específico
     * - versión de la API
     * 
     * Retorno este mapa directamente como JSON para que el cliente pueda visualizar
     * rápidamente la estructura de la API y los endpoints disponibles.
     */
    @GetMapping("/")
    public Map<String, Object> home() {

        // Creo un LinkedHashMap para mantener el orden de los elementos al devolverlos
        Map<String, Object> response = new LinkedHashMap<>(); // revisar opciones como por ej.: HashMap o TreeMap

        response.put("titulo", "Full Stack III - API Catalogo Biblioteca");
        response.put("mensaje", "Tarea S1");
        response.put("listado", "/catalogos - Lista de libros disponibles");
        response.put("individual", "/catalogos/{id} - Lista de libro individual");
        response.put("version", "1.0");

        return response;
    }
}


