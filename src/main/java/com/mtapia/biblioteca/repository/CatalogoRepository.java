package com.mtapia.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtapia.biblioteca.model.Catalogo;

// -------------------- INTERFAZ -------------------------

/**
 * Repositorio que permite realizar operaciones CRUD sobre la entidad Pelicula.
 * 
 * Al heredar de JpaRepository automáticamente Spring creará:
 * - findAll()
 * - findById()
 * - save()
 * - delete()
 * - y otros métodos útiles
 * 
 * Este repositorio conecta directamente con la tabla "peliculas" de Oracle.
 */
@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    // Por ahora no necesitamos agregar métodos adicionales.
    // Solo con la herencia ya tenemos disponibles todas las operaciones básicas.
}
