package com.mtapia.biblioteca.model;

// -------------------- IMPORTACIONES -------------------------

// Importamos una anotación que nos permite definir en qué orden queremos que aparezcan los atributos en el JSON de salida
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// Importamos para poder usar fechas y horas en Java
import java.time.LocalDateTime;

// Importamos la clase List, necesaria para manejar listas de cualquier tipo, en este caso de Películas o cualquier otro dato genérico
import java.util.List;

// -------------------- CLASE -------------------------

/**
 * Clase genérica que encapsula las respuestas REST del microservicio.
 * Su propósito es estructurar las respuestas que enviaremos al cliente de forma
 * ordenada y profesional.
 * 
 * Utilizamos Generics <T> para que este wrapper funcione con cualquier tipo de
 * dato (películas, usuarios, etc.).
 */
@JsonPropertyOrder({ "status", "cantidad", "timestamp", "data" }) // Esto define explícitamente el orden de los campos
public class ResponseWrapper<T> {
    // -------------------- ATRIBUTOS -------------------------

    // Indica si la operación fue exitosa u otro estado (por ejemplo: OK, ERROR)
    private String status;

    // Cantidad de elementos devueltos en la lista de datos
    private int cantidad;

    // Fecha y hora exacta en la que se genera la respuesta
    private LocalDateTime timestamp;

    // Lista de datos devueltos (películas u otros objetos)
    private List<T> data;

    // -------------------- CONSTRUCTOR -------------------------

    /**
     * Constructor que inicializa todos los campos de la respuesta.
     * Además, el timestamp se genera automáticamente con la fecha y hora actual.
     */
    public ResponseWrapper(String status, int cantidad, List<T> data) {
        this.status = status;
        this.cantidad = cantidad;
        this.timestamp = LocalDateTime.now(); // Se asigna automáticamente al momento de crear la respuesta
        this.data = data;
    }

    // -------------------- GETTERS y SETTERS -------------------------

    // Permiten acceder y modificar los atributos si es necesario

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}

/*
 * ¿Qué es esta clase?
 * 
 * Esta clase es la encargada de envolver las respuestas de nuestra API para que
 * siempre tengan un formato uniforme y claro.
 * 
 * ¿Qué es un Generic <T>?
 * 
 * Significa que ResponseWrapper es reutilizable con cualquier tipo de datos, no
 * solo películas.
 * 
 * ¿Por qué usamos timestamp?
 * 
 * Para registrar la fecha y hora exacta de cada respuesta, lo cual es una buena
 * práctica en APIs REST reales.
 * 
 * ¿Qué hace @JsonPropertyOrder()?
 * 
 * Controla el orden en que queremos que aparezcan los campos en el JSON,
 * garantizando que siempre salgan en este orden:
 * 
 * 
 * status → cantidad → timestamp → data
 * ¿Por qué usamos ResponseWrapper?
 * 
 * Porque en APIs reales nunca devolvemos las listas solas, siempre las enviamos
 * acompañadas de metadatos importantes como:
 * 
 * ¿Fue exitosa la operación?
 * 
 * ¿Cuántos registros devuelve?
 * 
 * ¿Cuándo se devolvieron?
 */