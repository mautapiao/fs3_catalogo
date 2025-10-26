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

/**
 * Controlador encargado de gestionar las solicitudes relacionadas con los
 * catálogos.
 * En este controlador expongo endpoints bajo el path "/catalogos".
 * Utilizo la anotación @RestController para indicar que esta clase provee
 * respuestas
 * directamente en formato JSON. Además, uso @Slf4j para contar con un logger
 * integrado.
 */
@Slf4j
@RestController
@RequestMapping("/catalogos")
public class CatalogoController {

    // Servicio donde se encuentra la lógica de negocio asociada a catálogos.
    private final CatalogoService catalogoService;

    // Este assembler me permite transformar mis entidades de dominio
    // a modelos que serán devueltos hacia el cliente (DTOs con formato HATEOAS u
    // otros).
    private final CatalogoModelAssembler catalogoModelAssembler;

    /**
     * Constructor del controlador.
     * Aquí inyecto las dependencias necesarias para trabajar:
     * - catalogoService: para realizar operaciones y consultas asociadas a
     * catálogos.
     * - catalogoModelAssembler: para convertir los resultados a modelos de salida.
     */
    public CatalogoController(CatalogoService catalogoService,
            CatalogoModelAssembler catalogoModelAssembler) {

        this.catalogoService = catalogoService;
        this.catalogoModelAssembler = catalogoModelAssembler;
    }

    /**
     * Método para obtener todos los registros de catálogos.
     * Aquí expongo un endpoint GET sin parámetros, el cual devuelve una colección
     * de catálogos.
     * Primero registro en el log la solicitud recibida para fines de trazabilidad.
     * Luego invoco al servicio para recuperar la lista completa.
     * 
     * Si la lista viene vacía, retorno un código HTTP 404 indicando que no existen
     * registros,
     * junto con una colección vacía como respuesta.
     * 
     * Si existen registros, los transformo utilizando el assembler, el cual me
     * permite
     * convertir cada entidad a un EntityModel, agregando enlaces HATEOAS cuando
     * corresponda.
     * Finalmente, retorno la colección junto con un enlace self hacia este mismo
     * endpoint.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Catalogo>>> obtenerTodas() {
        log.info("GET /catalogos - Obteniendo todos los registros");

        List<Catalogo> catalogos = catalogoService.obtenerTodas();

        if (catalogos.isEmpty()) {
            log.warn("No existen catálogos registrados actualmente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CollectionModel.empty());
        }

        // Transformo cada catálogo a su modelo representacional
        List<EntityModel<Catalogo>> listados = catalogos.stream()
                .map(catalogoModelAssembler::toModel)
                .collect(Collectors.toList());

        // Devuelvo la colección completa junto con el enlace self
        return ResponseEntity.ok(CollectionModel.of(listados,
                linkTo(methodOn(CatalogoController.class).obtenerTodas()).withSelfRel()));
    }

    /**
     * Método para obtener un catálogo específico según su ID.
     * En este endpoint recibo el identificador como parte de la ruta,
     * utilizando @PathVariable
     * para poder asignarlo al parámetro 'id'.
     * 
     * Primero registro en el log la acción que se está realizando, indicando el ID
     * consultado.
     * Luego delego en el servicio la búsqueda del registro. Si el registro no
     * existe, el servicio
     * se encarga de lanzar la excepción correspondiente (normalmente una
     * EntityNotFound o similar),
     * lo que luego será manejado por el manejador global de excepciones de la
     * aplicación.
     * 
     * Si el catálogo es encontrado, utilizo el assembler para transformarlo a su
     * representación
     * modelo (EntityModel), lo que permite agregar enlaces HATEOAS antes de
     * devolverlo.
     * Finalmente, retorno una respuesta HTTP 200 OK con el recurso encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /catalogos/{} - Buscando por ID", id);

        Catalogo catalogo = catalogoService.obtenerPorId(id);

        return ResponseEntity.ok(catalogoModelAssembler.toModel(catalogo));
    }

    /**
     * Método para crear un nuevo registro de catálogo.
     * Utilizo @PostMapping porque este endpoint se encarga de recibir y almacenar
     * un nuevo recurso.
     * El objeto Catalogo llega en el cuerpo de la solicitud (request body), por lo
     * que aplico
     * 
     * @RequestBody para mapearlo, y @Valid para asegurar que cumpla con las
     *              validaciones definidas
     *              en la entidad
     * 
     *              Primero registro en el log el intento de creación, mostrando el
     *              título del catálogo recibido.
     *              Luego delego en el servicio la operación de guardado. Si el
     *              proceso es exitoso, construyo la
     *              respuesta con código HTTP 201, y transformo el catálogo creado
     *              en un EntityModel
     *              usando el assembler, lo que permite adjuntar enlaces HATEOAS al
     *              recurso.
     * 
     *              En caso de que ocurra alguna excepción (por ejemplo, error de
     *              validación o integridad),
     *              capturo el error, lo registro y construyo un objeto de respuesta
     *              que indica el problema.
     *              Además, agrego un enlace para reintentar hacia la ruta principal
     *              de catálogos.
     */
    @PostMapping
    public ResponseEntity<EntityModel<Catalogo>> crearCatalogo(@Valid @RequestBody Catalogo nuevoCatalogo) {

        try {
            log.info("POST /catalogos - Creando catalogo: {}", nuevoCatalogo.getTitulo());

            // Guardo el nuevo catálogo en la base de datos
            Catalogo catalogoCreado = catalogoService.guardar(nuevoCatalogo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(catalogoModelAssembler.toModel(catalogoCreado));

        } catch (Exception e) {

            // Construyo un modelo de respuesta indicando el error
            Catalogo errorCatalogo = new Catalogo();
            errorCatalogo.setTitulo("Error: " + e.getMessage());

            EntityModel<Catalogo> errorModel = catalogoModelAssembler.toModel(errorCatalogo)
                    .add(Link.of("/catalogos").withRel("retry"));

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorModel);
        }
    }

    /**
     * Método para actualizar parcialmente un catálogo existente.
     * Este endpoint utiliza @PatchMapping porque solo modifica los campos indicados
     * en la solicitud,
     * sin reemplazar todo el recurso.
     * 
     * Recibo el ID del catálogo a través de la ruta (@PathVariable) y los campos a
     * actualizar
     * en un mapa genérico (@RequestBody Map<String, Object>), aplicando @Valid para
     * validar los datos.
     * 
     * Primero registro en el log la acción, indicando el ID del catálogo que se va
     * a actualizar.
     * Luego obtengo el catálogo existente desde la base de datos mediante el
     * servicio.
     * 
     * Recorro los campos recibidos y actualizo únicamente aquellos presentes en el
     * mapa,
     * dejando los demás valores intactos. Esto permite hacer actualizaciones
     * parciales
     * sin necesidad de enviar todo el objeto.
     * 
     * Una vez aplicados los cambios, guardo el objeto actualizado en la base de
     * datos
     * a través del servicio y retorno la versión transformada a EntityModel
     * para incluir enlaces HATEOAS, con un código HTTP 200 OK.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> actualizarParcialCatalogo(@PathVariable Long id,
            @Valid @RequestBody Map<String, Object> camposActualizados) {

        log.info("PATCH /catalogos/{} - Actualizando registro", id);

        // Obtener el catálogo existente
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

        // Guardar los cambios en la base de datos
        Catalogo actualizado = catalogoService.guardar(catalogoExistente);

        return ResponseEntity.ok(catalogoModelAssembler.toModel(actualizado));
    }

    /**
     * Método para actualizar completamente un catálogo existente.
     * Este endpoint utiliza @PutMapping porque reemplaza la información del recurso
     * con los datos recibidos en la solicitud.
     * 
     * Recibo el ID del catálogo a actualizar a través de la ruta (@PathVariable) y
     * el objeto completo Catalogo con los datos actualizados en el cuerpo de la
     * solicitud (@RequestBody),
     * aplicando @Valid para asegurar que cumpla con las validaciones definidas en
     * la entidad.
     * 
     * Primero registro en el log la operación indicando el ID que se va a
     * actualizar.
     * Luego delego en el servicio la operación de actualización, que se encarga de
     * reemplazar el registro en la base de datos y devolver el objeto actualizado.
     * 
     * Finalmente, transformo el catálogo actualizado en un EntityModel usando el
     * assembler
     * para incluir enlaces HATEOAS y devuelvo una respuesta HTTP 200 OK con el
     * recurso actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Catalogo>> actualizarCatalogo(@PathVariable Long id,
            @Valid @RequestBody Catalogo catalogoActualizado) {

        log.info("PUT /catalogos/{} - Actualizando registro", id);

        Catalogo actualizado = catalogoService.actualizar(id, catalogoActualizado);

        return ResponseEntity.ok(catalogoModelAssembler.toModel(actualizado));
    }

    /**
     * Método para eliminar un catálogo existente por su ID.
     * Este endpoint utiliza @DeleteMapping porque elimina un recurso específico.
     * 
     * Recibo el ID del catálogo a eliminar a través de la ruta (@PathVariable).
     * Primero verifico que el catálogo existe invocando el servicio; de esta
     * manera,
     * puedo obtener información relevante como el título antes de eliminarlo.
     * 
     * Registro en el log la acción de eliminación, indicando el ID del registro.
     * Luego intento eliminar el catálogo a través del servicio.
     * 
     * Si la operación es exitosa, devuelvo un ResponseWrapper con un mensaje
     * informativo
     * indicando que el catálogo fue eliminado correctamente y un código 0
     * (indicando éxito).
     * 
     * En caso de producirse algún error (por ejemplo, el registro no existe o hay
     * restricciones
     * de integridad), capturo la excepción y devuelvo un ResponseWrapper con un
     * mensaje de error
     * y un código de estado HTTP 400 (Bad Request), manteniendo la consistencia en
     * la respuesta.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarCatalogo(@PathVariable Long id) {

        // Verificar si existe el catálogo
        Catalogo catalogo = catalogoService.obtenerPorId(id);
        String titulo = catalogo.getTitulo();

        try {
            log.warn("DELETE /catalogos/{} - Eliminando registro", id);

            // Eliminar el catálogo
            catalogoService.eliminar(id);

            // Devolver mensaje de éxito
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Catálogo '" + titulo + "' eliminado con éxito",
                            0,
                            List.of()));

        } catch (Exception e) {
            // Manejar errores (ej., datos inválidos o restricciones)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>(
                            "Catálogo '" + titulo + "' no pudo ser eliminado",
                            0,
                            List.of()));
        }
    }

}
