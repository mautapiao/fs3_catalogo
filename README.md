# 📚 Biblioteca API - Catálogo de Libros

API REST desarrollada en **Spring Boot 3.5.7** para la gestión de un catálogo de libros. Proyecto desarrollado como parte del taller Full Stack III en **DUOC UC**.

---

## 🎯 Descripción del Proyecto

Biblioteca API es una aplicación backend que permite gestionar un catálogo de libros con operaciones CRUD completas. Implementa las mejores prácticas de desarrollo con Spring Boot, incluyendo HATEOAS, validaciones, manejo de errores y datos iniciales.

La aplicación incluye 20 libros precargados en categorías como Spring Boot, Java, Base de Datos, Angular, Seguridad y Calidad de Software.

---

## 🚀 Características Principales

- ✅ **CRUD Completo** - Crear, leer, actualizar y eliminar libros
- ✅ **HATEOAS** - Enlaces automáticos en respuestas JSON
- ✅ **Validaciones** - Con mensajes personalizados
- ✅ **Manejo de Errores** - Excepciones personalizadas
- ✅ **Datos Iniciales** - DataInitializer con 20 libros
- ✅ **Logs** - Con Slf4j y Lombok
- ✅ **Base de Datos Oracle** - Integración con Oracle 19c
- ✅ **Constructor Injection** - Con @RequiredArgsConstructor de Lombok

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión |
|-----------|---------|
| Java | 17 |
| Spring Boot | 3.5.7 |
| Spring Data JPA | 3.5.7 |
| Spring HATEOAS | 3.5.7 |
| Lombok | 1.18.30 |
| Hibernate | 6.6.33.Final |
| Oracle JDBC | 23.7.0.25.01 |
| Maven | 3.x |

---

## 📋 Requisitos

- **Java 17** o superior
- **Maven 3.6+** (o usar `./mvnw`)
- **Oracle Database 19c+** con usuario `webmastermto`
- **Postman** (opcional, para probar la API)

---

## ⚙️ Instalación y Configuración

### 1. Clonar el Proyecto

```bash
git clone https://github.com/tu-usuario/biblioteca.git
cd biblioteca
```

### 2. Configurar Base de Datos

Edita `src/main/resources/application.properties`:

```properties
# Datasource Oracle
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:ORCL
spring.datasource.username=webmastermto
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle12cDialect
```

### 3. Instalar Dependencias

```bash
./mvnw clean install -DskipTests
```

### 4. Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 📚 Estructura del Proyecto

```
src/main/java/com/mtapia/biblioteca/
├── BibliotecaApplication.java      # Clase principal
├── config/
│   └── DataInitializer.java        # Precarga de 20 libros
├── model/
│   └── Catalogo.java               # Entidad JPA
├── repository/
│   └── CatalogoRepository.java      # Interfaz JPA Repository
├── service/
│   └── CatalogoService.java         # Lógica de negocio
├── controller/
│   └── CatalogoController.java      # Endpoints REST
├── exception/
│   └── CatalogoNotFoundException.java # Excepción personalizada
└── hateoas/
    ├── CatalogoModelAssembler.java  # HATEOAS links
    └── ResponseWrapper.java         # Wrapper de respuestas
```

---

## 🔌 Endpoints de la API

### 1. Obtener Todos los Libros

```bash
GET http://localhost:8080/catalogos
```

**Respuesta:**
```json
{
  "_embedded": {
    "catalogoList": [
      {
        "id": 1,
        "titulo": "Spring Boot in Action",
        "autor": "Craig Walls",
        "anioPublicacion": 2015,
        "genero": "Spring Boot",
        "_links": {
          "self": { "href": "http://localhost:8080/catalogos/1" },
          "delete": { "href": "http://localhost:8080/catalogos/1" },
          "update": { "href": "http://localhost:8080/catalogos/1" },
          "all": { "href": "http://localhost:8080/catalogos" }
        }
      }
    ]
  }
}
```

### 2. Obtener Libro por ID

```bash
GET http://localhost:8080/catalogos/1
```

### 3. Crear Nuevo Libro

```bash
POST http://localhost:8080/catalogos
Content-Type: application/json

{
  "titulo": "Mi Nuevo Libro",
  "autor": "Juan Pérez",
  "anioPublicacion": 2024,
  "genero": "Ficción"
}
```

### 4. Actualizar Completo (PUT)

```bash
PUT http://localhost:8080/catalogos/1
Content-Type: application/json

{
  "titulo": "Spring Boot in Action - Edición 2",
  "autor": "Craig Walls",
  "anioPublicacion": 2024,
  "genero": "Spring Boot"
}
```

### 5. Actualizar Parcial (PATCH)

```bash
PATCH http://localhost:8080/catalogos/1
Content-Type: application/json

{
  "titulo": "Spring Boot in Action - Edición 2",
  "anioPublicacion": 2024
}
```

### 6. Eliminar Libro

```bash
DELETE http://localhost:8080/catalogos/1
```

**Respuesta:**
```json
{
  "mensaje": "Catalogo 'Spring Boot in Action' eliminado con éxito",
  "codigo": 0,
  "datos": []
}
```

---

## 📖 Modelo de Datos

### Entidad: Catalogo

| Campo | Tipo | Restricciones |
|-------|------|---|
| id | Long | PK, Auto-generado |
| titulo | String | NOT NULL, 1-200 caracteres |
| autor | String | NOT NULL, 1-200 caracteres |
| anioPublicacion | Integer | NOT NULL, 1000-2999 |
| genero | String | NOT NULL, 1-75 caracteres |

---

## 🐛 Validaciones

Las siguientes validaciones se aplican en la creación y actualización:

- **Título:** No puede estar vacío, máximo 200 caracteres
- **Autor:** No puede estar vacío, máximo 200 caracteres
- **Año de Publicación:** Debe estar entre 1000 y 2999
- **Género:** No puede estar vacío, máximo 75 caracteres

Ejemplo de error de validación:

```json
{
  "titulo": "El título debe tener entre 1 y 200 caracteres",
  "anioPublicacion": "Año debe ser mayor a 1000"
}
```

---

## 📊 Datos Iniciales

Al ejecutar la aplicación por primera vez, se insertan automáticamente **20 libros** en 6 categorías:

- **Spring Boot** (4 libros)
- **Java** (4 libros)
- **Base de Datos** (4 libros)
- **Angular** (3 libros)
- **Seguridad** (3 libros)
- **Calidad de Software** (2 libros)

Esto se configura en `DataInitializer.java`.

---

## 🔒 Manejo de Errores

### 404 - No Encontrado

```json
{
  "error": "Catalogo con id 999 no fue encontrado",
  "status": 404
}
```

### 400 - Solicitud Inválida

```json
{
  "error": "Ya existe un libro con ID 1",
  "status": 400
}
```

---

## 🧪 Testing con Postman

1. Importa los endpoints en Postman
2. Configura la variable `baseUrl = http://localhost:8080`
3. Prueba los 6 endpoints CRUD

**Colección de Postman:** Ver archivo `postman_collection.json`

---

## 📝 Notas Importantes

### Anotaciones Clave Utilizadas

- `@Entity` - Marca la clase como entidad JPA
- `@Repository` - Marca la interfaz como repositorio Spring
- `@Service` - Marca la clase como servicio de lógica de negocio
- `@RestController` - Marca la clase como controlador REST
- `@RequiredArgsConstructor` - Genera constructor con campos final (Lombok)
- `@Data` - Genera getters, setters, toString, equals, hashCode (Lombok)
- `@Slf4j` - Inyecta logger automáticamente (Lombok)

### Estructura de Paquetes

La estructura de paquetes es crítica. Spring debe escanear todos los paquetes:

```
com.mtapia.biblioteca/
├── config/
├── model/
├── repository/
├── service/
├── controller/
├── exception/
└── hateoas/
```

Si falta alguno, agregar `@EnableJpaRepositories` en la clase principal es una opción, mi error fue la estructura de carpetas.

---

## 🚀 Mejoras Futuras

- [ ] Agregar paginación y ordenamiento
- [ ] Implementar búsqueda avanzada
- [ ] Agregar autenticación JWT
- [ ] Implementar tests unitarios y de integración
- [ ] Documentación con Swagger/OpenAPI
- [ ] Docker y Docker Compose
- [ ] CI/CD con GitHub Actions

---

## 👨‍💻 Autor

Desarrollado por **Mauricio Tapia** como proyecto del taller Full Stack III en DUOC UC.

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

---

## 📞 Soporte

Si encuentras problemas:

1. Verifica que Oracle esté corriendo
2. Confirma las credenciales en `application.properties`
3. Ejecuta `mvn clean install -DskipTests`
4. Revisa los logs en la consola

---

**¡Gracias por usar Biblioteca API! 📚**