package com.mtapia.biblioteca.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

import com.mtapia.biblioteca.model.Catalogo;
import com.mtapia.biblioteca.repository.CatalogoRepository;

/*
 * Definición: @Configuration En este caso específico con DataInitializer, 
 * la anotación @Configuration indica a Spring que esta clase 
 * contiene definiciones de beans (en este caso, un CommandLineRunner)
 * que deben ser procesadas cuando se inicia la aplicación.
 */
@Slf4j
@Configuration
public class DataInitializer {

    // este codigo se ejecuta automaticamente al arrancar la aplicación
    // verifica si hay datos para no duplicar
    // si no hay datos crea algunos de prueba
    // similar a seeders de laravel, pero más sencillo
    @Bean
    CommandLineRunner initDatabase(
                                    CatalogoRepository catalogoRepository ) {
        return args -> {
           // Solo inserta datos si la tabla está vacía
            if (catalogoRepository.count() == 0) {
                log.info("Inicializando base de datos con libros...");
                
                List<Catalogo> libros = Arrays.asList(
                    // Spring Boot
                    new Catalogo(null, "Spring Boot in Action", "Craig Walls", 2015, "Spring Boot"),
                    new Catalogo(null, "Pro Spring Boot 2", "Felipe Gutierrez", 2018, "Spring Boot"),
                    new Catalogo(null, "Building Microservices with Spring Boot", "Mohan Raj", 2020, "Spring Boot"),
                    new Catalogo(null, "Spring Boot Cookbook", "Alex Antonov", 2015, "Spring Boot"),
                    
                    // Java
                    new Catalogo(null, "Effective Java", "Joshua Bloch", 2018, "Java"),
                    new Catalogo(null, "Head First Java", "Bert Bates", 2005, "Java"),
                    new Catalogo(null, "Java Concurrency in Practice", "Brian Goetz", 2006, "Java"),
                    new Catalogo(null, "Clean Code", "Robert C. Martin", 2008, "Java"),
                    
                    // Base de Datos
                    new Catalogo(null, "SQL Performance Explained", "Markus Winand", 2012, "Base de Datos"),
                    new Catalogo(null, "Database Design Manual", "Lightstone Storey", 2011, "Base de Datos"),
                    new Catalogo(null, "NoSQL Databases", "Pramod J. Sadalage", 2013, "Base de Datos"),
                    new Catalogo(null, "PostgreSQL: Up and Running", "Regina Obe", 2017, "Base de Datos"),
                    
                    // Angular
                    new Catalogo(null, "Angular in Action", "Jeremy Wilken", 2018, "Angular"),
                    new Catalogo(null, "Angular Development with TypeScript", "Yakov Fain", 2018, "Angular"),
                    new Catalogo(null, "Pro Angular", "Adam Freeman", 2018, "Angular"),
                    
                    // Seguridad
                    new Catalogo(null, "Spring Security in Action", "Laurentiu Spilcă", 2020, "Seguridad"),
                    new Catalogo(null, "OWASP Top 10", "OWASP Foundation", 2021, "Seguridad"),
                    new Catalogo(null, "Cryptography Engineering", "Schneier Ferguson", 2010, "Seguridad"),
                    
                    // Calidad de Software
                    new Catalogo(null, "The Pragmatic Programmer", "Andrew Hunt", 2019, "Calidad Software"),
                    new Catalogo(null, "Code Complete", "Steve McConnell", 2004, "Calidad Software")
                );
                
                catalogoRepository.saveAll(libros);
                log.info("✅ {} libros insertados correctamente", libros.size());
            } else {
                log.info("La base de datos ya contiene datos. Saltando inicialización.");
            }
        };
    }
}
