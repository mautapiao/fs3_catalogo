package com.mtapia.biblioteca.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Importa funciones para generar enlaces
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.mtapia.biblioteca.controller.CatalogoController;
import com.mtapia.biblioteca.model.Catalogo;

@Component
public class CatalogoModelAssembler implements RepresentationModelAssembler<Catalogo, EntityModel<Catalogo>> {
        
        /**
        * Este método transforma una modelo en un EntityModel con enlaces.
        * Incluye:
        * self Ver registro
        * delete Eliminar registro
        * update Actualizar registro
        * all Ver todos los registros
        */
        @Override
        @NonNull
        public EntityModel<Catalogo> toModel(@NonNull Catalogo catalogo) {
                return EntityModel.of(
                catalogo, // Entidad original
                
                // Enlace al detalle del registro (GET /catalogo/{id})
                linkTo(methodOn(CatalogoController.class)
                .obtenerPorId(catalogo.getId()))
                .withSelfRel(),
                
                // Enlace para eliminar (DELETE /catalogo/{id})
                linkTo(methodOn(CatalogoController.class)
                .eliminarCatalogo(catalogo.getId()))
                .withRel("delete"),
                
                // Enlace para actualizar (PUT /catalogo/{id}) – cuerpo ignorado aquí
                linkTo(methodOn(CatalogoController.class)
                .actualizarCatalogo(catalogo.getId(), null))
                .withRel("update"),
                
                // Enlace para ver todas los registros (GET /catalogos)
                linkTo(methodOn(CatalogoController.class)
                .obtenerTodas()) // todas las filas etc
                .withRel("all"));
        }
        
}
