package com.mtapia.biblioteca.controller;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtapia.biblioteca.hateoas.CatalogoModelAssembler;
import com.mtapia.biblioteca.model.Catalogo;
import com.mtapia.biblioteca.model.ResponseWrapper;
import com.mtapia.biblioteca.service.CatalogoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/catalogos")
public class CatalogoController {
    
    private final CatalogoService catalogoService;
    private final CatalogoModelAssembler catalogoModelAssembler;

    public CatalogoController(CatalogoService catalogoService,
            CatalogoModelAssembler catalogoModelAssembler) {

        this.catalogoService = catalogoService;
        this.catalogoModelAssembler = catalogoModelAssembler;

    }
    
    // Obtiene TODOS los registros.
    // Si la lista está vacía, devuelve un mensaje informativo.
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Catalogo>>> obtenerTodas() {
        log.info("GET /organizadores - Obteniendo todos los registros");

        List<Catalogo> catalogos = catalogoService.obtenerTodas();

        if (catalogos.isEmpty()) {
            log.warn("No hay libros registrados actualmente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CollectionModel.empty());
        }

        // Convertimos cada organizador a EntityModel y agregamos enlaces
        List<EntityModel<Catalogo>> listados = catalogos.stream()
                .map(catalogoModelAssembler::toModel)
                .collect(Collectors.toList());

        // Agregamos también un enlace al "self" del recurso (GET /catalogos)
        return ResponseEntity.ok(CollectionModel.of(listados,
                linkTo(methodOn(CatalogoController.class).obtenerTodas()).withSelfRel()));

    }
 
    // Obtiene registro según su ID.
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /catalogos/{} - Buscando por ID", id);

       Catalogo catalogo = catalogoService.obtenerPorId(id);

        return ResponseEntity.ok(catalogoModelAssembler.toModel(catalogo));
    }

    // Crear nuevo registro
    @PostMapping
    public ResponseEntity<EntityModel<Catalogo>> crearCatalogo(@Valid @RequestBody Catalogo nuevoCatalogo) {

        try {
            log.info("POST /catalogos - Creando catalogo: {}", nuevoCatalogo.getTitulo());

            // Guardar nuevo registro
            Catalogo catalogoCreado = catalogoService.guardar(nuevoCatalogo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(catalogoModelAssembler.toModel(catalogoCreado));

        } catch (Exception e) {

            // EntityModel con un objeto TipoMascota vacío o de error
            Catalogo errorCatalogo = new Catalogo();
            errorCatalogo.setTitulo("Error: " + e.getMessage());

            EntityModel<Catalogo> errorModel = catalogoModelAssembler.toModel(errorCatalogo)
                    .add(Link.of("/catalogos").withRel("retry"));
            
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorModel);
        }
    }

    @PatchMapping("/{id}")
public ResponseEntity<EntityModel<Catalogo>> actualizarParcialCatalogo(@PathVariable Long id,
        @Valid @RequestBody Map<String, Object> camposActualizados) {
    
    log.info("PATCH /catalogos/{} - Actualizando registro", id);
    
    // Obtener el objeto existente
    Catalogo catalogoExistente = catalogoService.obtenerPorId(id);
    
    // Actualizar solo los campos que vienen en la solicitud
    if (camposActualizados.containsKey("titulo")) {
        catalogoExistente.setTitulo((String) camposActualizados.get("titulo"));
    }
    
    if (camposActualizados.containsKey("autor")) {
        catalogoExistente.setAutor((String) camposActualizados.get("autor"));
    }
    
    if (camposActualizados.containsKey("anioPublicacion")) {
        catalogoExistente.setAnioPublicacion((Integer) camposActualizados.get("anioPublicacion"));
    }
    
    if (camposActualizados.containsKey("genero")) {
        catalogoExistente.setGenero((String) camposActualizados.get("genero"));
    }
    
    // GUARDAR los cambios en la BD
    Catalogo actualizado = catalogoService.guardar(catalogoExistente);
    
    return ResponseEntity.ok(catalogoModelAssembler.toModel(actualizado));
}

    // Actualizar PUT
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> actualizarCatalogo(@PathVariable Long id,
            @Valid @RequestBody Catalogo catalogoActualizado) {

        log.info("PUT /catalogos/{} - Actualizando registro", id);

        Catalogo actualizado = catalogoService.actualizar(id, catalogoActualizado);

        return ResponseEntity.ok(catalogoModelAssembler.toModel(actualizado));
    }

    // Eliminar Registro
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarCatalogo(@PathVariable Long id) {

        // Verificar si existe

        Catalogo catalogo = catalogoService.obtenerPorId(id);
        String titulo = catalogo.getTitulo();

        try {
            log.warn("DELETE /catalogos/{} - Eliminando registro", id);

            // Eliminar
            catalogoService.eliminar(id);

            // Devolver mensaje de éxito

            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Catalogo '" + titulo + "' eliminado con éxito",
                            0,
                            List.of()));

        } catch (Exception e) {
            // Manejar donde no existe, errores (por ejemplo, datos inválidos)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>(
                            "Catalogo '" + titulo + "'no pudo ser eliminado",
                            0,
                            List.of()));
        }
    }

}
