package com.mtapia.biblioteca.config;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mtapia.biblioteca.model.Catalogo;
import com.mtapia.biblioteca.repository.CatalogoRepository;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CatalogoRepository catalogoRepository) {
        return args -> {

            try {
                // Esperar brevemente por si la BD tarda en levantar
                Thread.sleep(1500);

                if (catalogoRepository.count() == 0) {

                    log.info("Inicializando base de datos con datos de catálogo...");

                    List<Catalogo> libros = Arrays.asList(

                            // Spring Boot
                            Catalogo.builder().titulo("Spring Boot in Action").autor("Craig Walls")
                                    .anioPublicacion(2015).genero("Spring Boot").build(),
                            Catalogo.builder().titulo("Pro Spring Boot 2").autor("Felipe Gutierrez")
                                    .anioPublicacion(2018).genero("Spring Boot").build(),
                            Catalogo.builder().titulo("Building Microservices with Spring Boot").autor("Mohan Raj")
                                    .anioPublicacion(2020).genero("Spring Boot").build(),
                            Catalogo.builder().titulo("Spring Boot Cookbook").autor("Alex Antonov")
                                    .anioPublicacion(2015).genero("Spring Boot").build(),

                            // Java
                            Catalogo.builder().titulo("Effective Java").autor("Joshua Bloch").anioPublicacion(2018)
                                    .genero("Java").build(),
                            Catalogo.builder().titulo("Head First Java").autor("Bert Bates").anioPublicacion(2005)
                                    .genero("Java").build(),
                            Catalogo.builder().titulo("Java Concurrency in Practice").autor("Brian Goetz")
                                    .anioPublicacion(2006).genero("Java").build(),
                            Catalogo.builder().titulo("Clean Code").autor("Robert C. Martin").anioPublicacion(2008)
                                    .genero("Java").build(),

                            // Base de Datos
                            Catalogo.builder().titulo("SQL Performance Explained").autor("Markus Winand")
                                    .anioPublicacion(2012).genero("Base de Datos").build(),
                            Catalogo.builder().titulo("Database Design Manual").autor("Lightstone Storey")
                                    .anioPublicacion(2011).genero("Base de Datos").build(),
                            Catalogo.builder().titulo("NoSQL Databases").autor("Pramod J. Sadalage")
                                    .anioPublicacion(2013).genero("Base de Datos").build(),
                            Catalogo.builder().titulo("PostgreSQL: Up and Running").autor("Regina Obe")
                                    .anioPublicacion(2017).genero("Base de Datos").build(),

                            // Angular
                            Catalogo.builder().titulo("Angular in Action").autor("Jeremy Wilken").anioPublicacion(2018)
                                    .genero("Angular").build(),
                            Catalogo.builder().titulo("Angular Development with TypeScript").autor("Yakov Fain")
                                    .anioPublicacion(2018).genero("Angular").build(),
                            Catalogo.builder().titulo("Pro Angular").autor("Adam Freeman").anioPublicacion(2018)
                                    .genero("Angular").build(),

                            // Seguridad
                            Catalogo.builder().titulo("Spring Security in Action").autor("Laurentiu Spilcă")
                                    .anioPublicacion(2020).genero("Seguridad").build(),
                            Catalogo.builder().titulo("OWASP Top 10").autor("OWASP Foundation").anioPublicacion(2021)
                                    .genero("Seguridad").build(),
                            Catalogo.builder().titulo("Cryptography Engineering").autor("Schneier Ferguson")
                                    .anioPublicacion(2010).genero("Seguridad").build(),

                            // Calidad Software
                            Catalogo.builder().titulo("The Pragmatic Programmer").autor("Andrew Hunt")
                                    .anioPublicacion(2019).genero("Calidad Software").build(),
                            Catalogo.builder().titulo("Code Complete").autor("Steve McConnell").anioPublicacion(2004)
                                    .genero("Calidad Software").build());

                    catalogoRepository.saveAll(libros);

                    log.info("📚 {} libros insertados correctamente", libros.size());

                } else {
                    log.info("La base de datos ya contiene datos. Se omite la inicialización.");
                }

            } catch (Exception e) {
                log.error("⚠️ Error durante la inicialización de datos: {}", e.getMessage(), e);
            }
        };
    }
}
