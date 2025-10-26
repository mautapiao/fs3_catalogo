package com.mtapia.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mtapia.biblioteca.exception.CatalogoNotFoundException;
import com.mtapia.biblioteca.model.Catalogo;
import com.mtapia.biblioteca.repository.CatalogoRepository;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogoService {

        private final CatalogoRepository catalogoRepository;

        // El constructor se genera automáticamente
        public List<Catalogo> obtenerTodas() {
                log.debug("Servicio: obtenerTodas()");

                return catalogoRepository.findAll(Sort.by("id").ascending());

        }

        public Catalogo obtenerPorId(Long id) {
                log.debug("Servicio: obtenerPorId({})", id);
                return catalogoRepository.findById(id)
                                .orElseThrow(() -> new CatalogoNotFoundException(id));
        }

        public Catalogo guardar(Catalogo catalogo) {
                log.debug("Servicio: guardar({})", catalogo.getTitulo());

                // Si tiene ID y existe en BD, es una ACTUALIZACIÓN (permitir)
                // Si tiene ID y NO existe, es un error
                // Si NO tiene ID, es un INSERT nuevo (permitir)

                // Solo validar si el ID no es null (es decir, es una actualización)
                if (catalogo.getId() != null && !catalogoRepository.existsById(catalogo.getId())) {
                        log.error("No existe un libro con ID {}", catalogo.getId());
                        throw new IllegalArgumentException("No existe un libro con ID " + catalogo.getId());
                }

                return catalogoRepository.save(catalogo);
        }

        public Catalogo actualizar(Long id, Catalogo catalogoActualizada) {
                log.debug("Servicio: actualizar({}, {})", id, catalogoActualizada.getTitulo());

                Catalogo existente = catalogoRepository.findById(id)
                                .orElseThrow(() -> new CatalogoNotFoundException(id));

                existente.setTitulo(catalogoActualizada.getTitulo());
                existente.setAutor(catalogoActualizada.getAutor());
                existente.setAnioPublicacion(catalogoActualizada.getAnioPublicacion());
                existente.setGenero(catalogoActualizada.getGenero());

                return catalogoRepository.save(existente);
        }

        public void eliminar(Long id) {
                log.debug("Servicio: eliminar({})", id);

                Catalogo existente = catalogoRepository.findById(id)
                                .orElseThrow(() -> new CatalogoNotFoundException(id));

                catalogoRepository.delete(existente);
        }

}
