package com.mtapia.biblioteca.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {

        Map<String, Object> response = new LinkedHashMap<>(); // HashMap<>() TreeMap<>();
        response.put("titulo", "Full Stack III - API Catalogo Biblioteca");
        response.put("mensaje", "Tarea S1");
        response.put("listado", "/catalogos - Lista de libros disponibles");
        response.put("individual", "/catalogos/{id} - Lista de libro individual");
        response.put("version", "1.0");

        return response;

    }

}