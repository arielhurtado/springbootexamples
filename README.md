# API RESTful, DTO y Comunicación de Microservicios
**Prof. Julio Ariel Hurtado Alegría**
**Ingeniería de Software — Grupo de Investigación IDIS**  
**Universidad del Cauca, Popayán, 2026**

---

## Tabla de contenido

- [Prefacio](#prefacio)
- [1. API RESTful](#1-api-restful)
  - [1.1 El problema que REST resuelve](#11-el-problema-que-rest-resuelve)
  - [1.2 Las restricciones de REST](#12-las-restricciones-de-rest)
  - [1.3 Recursos y URIs](#13-recursos-y-uris)
  - [1.4 Métodos HTTP en profundidad](#14-métodos-http-en-profundidad)
  - [1.5 Representaciones: JSON como formato de facto](#15-representaciones-json-como-formato-de-facto)
  - [1.6 Códigos de estado](#16-códigos-de-estado)
  - [1.7 Frameworks Java](#17-frameworks-java)
- [2. Persistencia con JPA](#2-persistencia-con-jpa)
  - [2.1 Por qué hablar de persistencia](#21-por-qué-hablar-de-persistencia)
  - [2.2 JPA: la especificación](#22-jpa-la-especificación)
  - [2.3 Entidades](#23-entidades)
  - [2.4 Repositorios con Spring Data](#24-repositorios-con-spring-data)
  - [2.5 Transacciones](#25-transacciones)
  - [2.6 El rol del DTO en la frontera](#26-el-rol-del-dto-en-la-frontera)
- [3. DTO — Data Transfer Object](#3-dto--data-transfer-object)
  - [3.1 Definición](#31-definición)
  - [3.2 Por qué no exponer entidades JPA](#32-por-qué-no-exponer-entidades-jpa)
  - [3.3 Ejemplo: entidad vs DTO](#33-ejemplo-entidad-vs-dto)
  - [3.4 DTOs de entrada y salida](#34-dtos-de-entrada-y-salida)
  - [3.5 DTOs en el repositorio de ejemplos](#35-dtos-en-el-repositorio-de-ejemplos)
- [4. Mapeo entre entidades y DTOs](#4-mapeo-entre-entidades-y-dtos)
  - [4.1 El problema del mapeo](#41-el-problema-del-mapeo)
  - [4.2 Mapeo manual](#42-mapeo-manual)
  - [4.3 MapStruct: el enfoque moderno](#43-mapstruct-el-enfoque-moderno)
  - [4.4 Comparación](#44-comparación)
- [5. Comunicación de microservicios](#5-comunicación-de-microservicios)
  - [5.1 Microservicios no son tiers](#51-microservicios-no-son-tiers)
  - [5.2 El ejemplo BookingGym-Sync](#52-el-ejemplo-bookinggym-sync)
  - [5.3 Comunicación síncrona](#53-comunicación-síncrona)
  - [5.4 Comunicación asíncrona](#54-comunicación-asíncrona)
  - [5.5 Idempotencia en consumidores](#55-idempotencia-en-consumidores)
- [6. Síntesis](#6-síntesis)
- [Referencias](#referencias)

---

## Prefacio

Este libro nace de una constatación sencilla: la mayoría de los materiales sobre microservicios explican los conceptos de manera aislada. Se habla de REST sin explicar por qué sus restricciones importan. Se habla de DTOs sin mostrar cómo se mapean desde entidades JPA. Se habla de comunicación sin distinguir cuándo conviene lo síncrono y cuándo lo asíncrono. El resultado es un conocimiento fragmentado que el estudiante no logra integrar.

Nuestra intención es distinta. Queremos construir un recorrido coherente que empiece por el diseño de la frontera de un servicio (la API RESTful), continúe por el modelado de los datos que cruzan esa frontera (los DTOs), y termine por los mecanismos que permiten a los servicios hablar entre sí (Feign, RabbitMQ). Todo ello con ejemplos concretos extraídos del repositorio público [springbootexamples](https://github.com/arielhurtado/springbootexamples), que contiene tres aplicaciones independientes pero complementarias:

- **DtoMappingManual**: un proyecto que ilustra el mapeo manual entre entidades JPA y DTOs.
- **DtoMappingMapstruct**: el mismo dominio, pero con MapStruct como estrategia de mapeo.
- **BookingGym-Sync**: un sistema de dos microservicios que colaboran por red, uno de ellos mediante Feign y RabbitMQ.

Cada capítulo del libro desarrolla una idea central, la explica con detalle, la ilustra con código y la conecta con el resto del contenido. Al final, el lector debería poder responder con solidez tres preguntas: ¿cómo se expone un servicio al mundo?, ¿cómo se modelan los datos que cruzan su frontera?, ¿cómo hablan entre sí los servicios que componen un sistema distribuido?

---

## 1. API RESTful

### 1.1 El problema que REST resuelve

Antes de definir REST conviene entender qué problema resuelve. A comienzos de la década de 2000, los sistemas distribuidos se construían con tecnologías como SOAP, CORBA o DCOM. Estas tecnologías funcionaban, pero arrastraban tres problemas recurrentes: eran pesadas (cada mensaje viajaba envuelto en capas de XML y metadatos), eran rígidas (un cambio en el contrato obligaba a regenerar los stubs en todos los clientes) y eran difíciles de escalar (mantenían estado de sesión en el servidor, lo que complicaba la replicación).

En 2000, Roy Fielding publicó su tesis doctoral *Architectural Styles and the Design of Network-based Software Architectures*, donde describió un estilo arquitectónico al que llamó **REST** (*Representational State Transfer*). La idea central era radicalmente simple: en lugar de inventar un protocolo nuevo, había que aprovechar el protocolo que ya estaba en todas partes —HTTP— y respetar sus reglas originales. La web ya funcionaba a escala planetaria; bastaba con aplicar sus mismos principios al diseño de APIs.

REST no es un estándar, ni un protocolo, ni una librería. Es un estilo arquitectónico: un conjunto de restricciones que, cuando se aplican de forma coherente, producen sistemas distribuidos escalables, evolutivos y tolerantes a fallos. Una API que respeta esas restricciones se dice *RESTful*.

### 1.2 Las restricciones de REST

#### Cliente-servidor

La primera restricción separa las responsabilidades. El cliente se ocupa de la interfaz de usuario; el servidor, del almacenamiento y la lógica de negocio. Esta separación permite que ambos evolucionen de forma independiente.

#### Sin estado

La segunda restricción es la más importante y la que más se malinterpreta. **Stateless** significa que cada solicitud del cliente debe contener toda la información necesaria para que el servidor la procese. El servidor no guarda nada entre solicitudes.

Esta restricción tiene una consecuencia enorme: cualquier servidor puede atender cualquier solicitud, porque no hay estado que preservar entre llamadas. Eso permite balancear carga, replicar servidores, reiniciarlos sin perder sesiones.

#### Cacheable

Las respuestas deben indicar si pueden almacenarse en caché. Cuando un cliente solicita un recurso que no ha cambiado, el servidor puede responder con un código `304 Not Modified` y el cliente reutiliza la copia que ya tenía.

#### Interfaz uniforme

Todos los recursos se manipulan con el mismo conjunto de operaciones: los métodos HTTP. No hay métodos específicos del dominio (`crearUsuario`, `borrarPedido`); hay `POST`, `DELETE`, etc., aplicados a URIs que identifican recursos.

#### Sistema por capas

El cliente no necesita saber si está hablando directamente con el servidor final o con un intermediario (proxy, balanceador, caché).

### 1.3 Recursos y URIs

En REST, todo lo que merece ser nombrado es un **recurso**. Cada recurso tiene un identificador único: su **URI**. La URI no describe una acción; nombra un sustantivo. La acción la aporta el método HTTP.

| Endpoint | Significado |
|---|---|
| `GET /usuarios` | Lista todos los usuarios |
| `GET /usuarios/5` | Obtiene el usuario con id 5 |
| `POST /usuarios` | Crea un nuevo usuario |
| `PUT /usuarios/5` | Reemplaza el usuario con id 5 |
| `PATCH /usuarios/5` | Actualiza parcialmente el usuario 5 |
| `DELETE /usuarios/5` | Elimina el usuario con id 5 |

### 1.4 Métodos HTTP en profundidad

#### GET

Solicita una representación de un recurso. Es **seguro** (no modifica el estado) e **idempotente** (ejecutarlo múltiples veces produce el mismo resultado).

#### POST

Crea un recurso nuevo dentro de una colección. No es idempotente: cada ejecución puede crear un recurso distinto.

```http
POST /usuarios
{ "nombre": "Juan Pérez", "correo": "juan@example.com" }

// Respuesta 201 Created
// Location: /usuarios/5
```

#### PUT

Reemplaza un recurso existente. El cliente conoce la URI completa y envía la representación completa. Es idempotente.

```http
PUT /usuarios/5
{ "nombre": "Juan Pérez", "correo": "juan@example.com" }

// N veces -> /usuarios/5 = { "nombre": "Juan Pérez", ... }
```

#### PATCH

Aplica una modificación parcial. No está garantizado como idempotente. Su idempotencia depende del tipo de parche:

- Si envía valores concretos (asignación), es idempotente: `PATCH /usuarios/5 { "email": "nuevo@x.com" }`.
- Si envía una operación relativa (incremento), no es idempotente: `PATCH /cuentas/5 { "delta": +100 }`.

La misma idea que separa `x = 5` de `x += 5` en programación separa un PATCH idempotente de uno que no lo es.

#### DELETE

Elimina un recurso. Es idempotente: eliminar el mismo recurso N veces produce el mismo estado final.

### 1.5 Representaciones: JSON como formato de facto

REST no impone un formato. El cliente y el servidor negocian mediante `Content-Type` y `Accept`. En la práctica, JSON se ha impuesto por tres razones: es ligero, es legible por humanos y es nativamente compatible con JavaScript.

```json
{
  "id": 5,
  "nombre": "Juan Pérez",
  "correo": "juan@example.com",
  "edad": 30,
  "activo": true,
  "roles": ["ADMIN", "USER"],
  "direccion": {
    "ciudad": "Popayán",
    "pais": "Colombia"
  }
}
```

### 1.6 Códigos de estado

| Código | Significado | Uso típico |
|---|---|---|
| 200 | OK | GET, PUT, PATCH exitosos |
| 201 | Created | POST exitoso |
| 204 | No Content | DELETE exitoso |
| 400 | Bad Request | Solicitud mal formada |
| 401 | Unauthorized | Falta autenticación |
| 403 | Forbidden | Autenticado pero sin permiso |
| 404 | Not Found | Recurso inexistente |
| 409 | Conflict | Conflicto de estado |
| 500 | Internal Server Error | Error inesperado del servidor |

### 1.7 Frameworks Java

- **Spring Boot**: el más popular. Ecosistema enorme, comunidad activa, integración natural con Spring Data, Spring Security y Spring Cloud.
- **Quarkus**: orientado a cloud-native. Arranque rápido y bajo consumo de memoria.
- **Micronaut**: resuelve la inyección de dependencias en tiempo de compilación.
- **MicroProfile**: estándar neutral de proveedor para Jakarta EE.

---

## 2. Persistencia con JPA

### 2.1 Por qué hablar de persistencia

Un microservicio encapsula una capacidad de negocio y su estado. Sin persistencia, un servicio es puro cálculo sin memoria.

En un monolito, todas las tablas viven en una misma base de datos y los joins son triviales. En microservicios, cada servicio es dueño de su propia base de datos (patrón *Database per Service*). No hay joins cruzados; la consistencia entre servicios es eventual.

Esta decisión tiene un beneficio enorme: cada servicio es autónomo. Su equipo elige el motor de base de datos, el esquema, la estrategia de migración. El costo es que las consultas que en un monolito se resolvían con un join, ahora requieren llamadas de red o duplicación controlada de datos.

### 2.2 JPA: la especificación

**Java Persistence API (JPA)** es la especificación estándar de Java para mapear objetos a tablas relacionales. Esa técnica se conoce como *Object-Relational Mapping* (ORM). JPA no es una implementación; es un conjunto de interfaces y anotaciones. Las implementaciones más conocidas son Hibernate, EclipseLink y OpenJPA.

### 2.3 Entidades

```java
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editorial_id")
    private Editorial editorial;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    // getters y setters
}
```

Anotaciones clave:

- `@Entity`: indica que esta clase es una entidad JPA.
- `@Table`: especifica el nombre de la tabla en la base de datos.
- `@Id`: marca el atributo que actúa como clave primaria.
- `@GeneratedValue`: indica que la base de datos genera el valor del identificador.
- `@ManyToOne`: relación muchos a uno.
- `@ManyToMany`: relación muchos a muchos.
- `fetch = FetchType.LAZY`: la relación se carga de forma perezosa.

### 2.4 Repositorios con Spring Data

```java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Query method: Spring deriva la consulta del nombre
    List<Book> findByTitle(String title);

    // Consulta personalizada con JPQL
    @Query("SELECT b FROM Book b WHERE b.editorial.name = :name")
    List<Book> findByEditorialName(@Param("name") String name);
}
```

El desarrollador no escribe SQL. Spring Data genera la consulta a partir del nombre del método. `findByTitle` se traduce a `SELECT * FROM books WHERE title = ?`.

### 2.5 Transacciones

```java
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
```

El atributo `readOnly = true` indica que la transacción no modificará datos, lo que permite a Hibernate optimizar ciertas operaciones.

### 2.6 El rol del DTO en la frontera

¿Debemos exponer entidades JPA directamente en la API? La respuesta es no. Una entidad JPA contiene relaciones, anotaciones y detalles del esquema que no queremos exponer. Peor aún, las relaciones bidireccionales pueden provocar ciclos infinitos durante la serialización JSON. Y cualquier cambio en el esquema de la base de datos rompería el contrato de la API. La solución es introducir un objeto intermedio: el **DTO**.

---

## 3. DTO — Data Transfer Object

### 3.1 Definición

Un **DTO** (*Data Transfer Object*) es un objeto que se usa para transferir datos entre capas de una aplicación. Su única responsabilidad es definir la forma de los datos que cruzan una frontera. No contiene lógica de negocio, no tiene relaciones JPA, no depende de la base de datos.

El término proviene del libro *Patterns of Enterprise Application Architecture* de Martin Fowler. En el contexto de las APIs RESTful, el DTO cumple una función adicional: desacoplar el contrato público de la API del modelo interno de persistencia.

### 3.2 Por qué no exponer entidades JPA

- **Exposición de la estructura interna**: el cliente ve las tablas, las columnas, las claves foráneas.
- **Problemas de serialización**: las relaciones bidireccionales provocan ciclos infinitos (`StackOverflowError`).
- **Acoplamiento**: cualquier cambio en el esquema de la base de datos rompe el contrato.
- **Seguridad**: campos sensibles (contraseñas, tokens) pueden quedar expuestos.

### 3.3 Ejemplo: entidad vs DTO

```java
// Entidad JPA
@Entity
public class Book {
    @Id private Long id;
    private String title;
    @ManyToOne private Editorial editorial;
    @ManyToMany private List<Author> authors;
}

// DTO de salida
public class BookDto {
    private Long id;
    private String title;
    private String editorial;      // solo el nombre
    private List<String> authors;  // solo los nombres
}
```

El DTO aplana las relaciones y expone solo lo que el cliente necesita.

### 3.4 DTOs de entrada y salida

Es una buena práctica separar los DTOs de entrada de los de salida.

```java
// DTO de salida
public class BookDto {
    private Long id;
    private String title;
    private String editorial;
    private List<String> authors;
}

// DTO de entrada
public class BookCreateDto {
    @NotBlank
    private String title;

    @NotNull
    private Long editorialId;

    @NotEmpty
    private List<Long> authorIds;
}
```

El DTO de entrada no incluye `id` (lo asigna el servidor) y usa identificadores en lugar de objetos completos para las relaciones.

### 3.5 DTOs en el repositorio de ejemplos

En el proyecto **DtoMappingManual**, el DTO `BookDto` se construye a partir de una entidad `Book` mediante un constructor:

```java
public class BookDto {

    private Long id;
    private String title;
    private String editorial;
    private List<String> authors;

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.editorial = Optional.ofNullable(book.getEditorial())
                .map(Editorial::getName)
                .orElse(null);
        this.authors = book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }

    // getters y setters
}
```

Este enfoque es directo y no requiere dependencias adicionales. Su problema es que la lógica de conversión queda dispersa y crece linealmente con el modelo.

---

## 4. Mapeo entre entidades y DTOs

### 4.1 El problema del mapeo

Convertir una entidad en un DTO y viceversa es una operación mecánica, repetitiva y propensa a errores.

```java
public BookDto toDto(Book book) {
    BookDto dto = new BookDto();
    dto.setId(book.getId());
    dto.setTitle(book.getTitle());
    dto.setEditorial(book.getEditorial().getName());
    dto.setAuthors(book.getAuthors().stream()
                       .map(Author::getName).toList());
    return dto;
}
```

Escribir esto para cada entidad del sistema es tedioso. Además, es código que no aporta valor de negocio.

### 4.2 Mapeo manual

En el proyecto **DtoMappingManual**, el mapeo vive en el constructor del DTO.

```java
// BookService.java
@Override
@Transactional
public List<BookDto> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    return books.stream()
            .map(BookDto::new)   // referencia al constructor
            .collect(Collectors.toList());
}
```

Es elegante por su simplicidad, pero no escala bien.

### 4.3 MapStruct: el enfoque moderno

**MapStruct** es un procesador de anotaciones que genera implementaciones de mapeo en tiempo de compilación. A diferencia de otras librerías como ModelMapper, que usan reflexión, MapStruct genera código Java plano.

```java
@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "editorial.name", target = "editorial")
    @Mapping(source = "authors", target = "authors",
             qualifiedByName = "mapAuthorsToNames")
    BookDto toDto(Book book);

    @Named("mapAuthorsToNames")
    default List<String> mapAuthorsToNames(List<Author> authors) {
        return authors.stream().map(Author::getName).toList();
    }

    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(BookDto bookDto);
}
```

El mapper se inyecta en el servicio:

```java
@Service
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookService(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book no encontrado"));
        return bookMapper.toDto(book);
    }
}
```

### 4.4 Comparación

| Aspecto | Mapeo manual | MapStruct |
|---|---|---|
| Dónde vive el mapeo | Constructor o método `toDto()` | Interfaz anotada con `@Mapper` |
| Código repetitivo | Alto | Nulo (se genera) |
| Rendimiento | N/A | Rápido (Java plano, sin reflexión) |
| Seguridad en compilación | Depende del desarrollador | Sí (falla al compilar con errores) |
| Curva de aprendizaje | Baja | Media |
| Mantenibilidad | Pobre a gran escala | Excelente |

---

## 5. Comunicación de microservicios

### 5.1 Microservicios no son tiers

La confusión más común es creer que los microservicios son una evolución de la arquitectura de tres capas. No lo son. Los tiers descomponen por **capa técnica**; los microservicios, por **capacidad de negocio**.

| Pregunta | Tiers | Microservicios |
|---|---|---|
| ¿Qué descompone? | Capa técnica | Capacidad de negocio |
| ¿Cuántos componentes? | 3 típicamente | Muchos (10+) |
| ¿Confían entre sí? | Sí | No |
| Base de datos | Compartida | Una por servicio |
| Comunicación | Jerárquica | Por red (REST, colas) |
| Despliegue | Monolítico | Independiente por servicio |

### 5.2 El ejemplo BookingGym-Sync

El proyecto **BookingGym-Sync** ilustra este concepto con dos microservicios:

- **MS Booking** (puerto 8080): gestiona las reservas de clases de gimnasio.
- **MS Information** (puerto 8081): gestiona la información de usuarios.

Cada uno es una aplicación Spring Boot independiente, con su propio `pom.xml`, su propio `main()` y su propio modelo de datos.

### 5.3 Comunicación síncrona

La comunicación síncrona sigue el modelo petición-respuesta.

- Acoplamiento temporal fuerte.
- Latencia acumulada.
- Fallos en cascada.
- Simplicidad de depuración.

#### Feign: clientes HTTP declarativos

```java
@FeignClient(name = "information-microservice",
             url = "http://localhost:8081/api/information")
public interface ServiceInformationClient {

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}
```

Para habilitar Feign:

```java
@SpringBootApplication
@EnableFeignClients
public class BookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
```

Uso en el servicio:

```java
@Service
public class BookingService {

    @Autowired
    private ServiceInformationClient serviceInformationClient;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public Booking createBooking(Long userId, Long gymClassId,
                                 Booking booking) {
        try {
            User user = serviceInformationClient.getUserById(userId);
            booking.setUser(user);
            booking.setUserId(userId);
            return bookingRepository.save(booking);
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(
                "El usuario con ID " + userId + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException(
                "Error al invocar el microservicio: " + e.getMessage(), e);
        }
    }
}
```

#### Cuándo usar comunicación síncrona

- El llamador necesita la respuesta de inmediato.
- La interacción es secuencial y transaccional.
- La API se expone a clientes externos.

### 5.4 Comunicación asíncrona

La comunicación asíncrona desacopla temporalmente al emisor del receptor.

- Acoplamiento temporal débil.
- Resiliencia: los mensajes se acumulan en la cola.
- Complejidad de infraestructura.
- Consistencia eventual.

#### RabbitMQ: configuración

```java
@Configuration
public class RabbitMQConfig {

    public static final String USER_QUEUE = "userQueue";

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);  // durable
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(jsonMessageConverter());
        return t;
    }
}
```

#### Consumidor

```java
@Service
public class UserConsumerService {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receive(User user) {
        userRepository.save(user);
        System.out.println("Recibido: " + user.getFirstName());
    }
}
```

#### Productor

```java
@Service
public class UserService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.USER_QUEUE, user);
        return userRepository.save(user);
    }
}
```

#### Cuándo usar comunicación asíncrona

- La respuesta no es inmediata.
- El trabajo puede ejecutarse en segundo plano.
- Se necesita resiliencia y desacoplamiento.

### 5.5 Idempotencia en consumidores

Un consumidor asíncrono debe ser idempotente: si el mismo mensaje se procesa dos veces, el resultado debe ser el mismo. RabbitMQ garantiza *at-least-once delivery*, lo que significa que un mensaje puede entregarse más de una vez si hay fallos de red o reinicios.

Una estrategia común es incluir un identificador único en cada mensaje y llevar un registro de los ya procesados.

---

## 6. Síntesis

### Cómo encajan los tres temas

- La **API RESTful** define *cómo* se expone un servicio al mundo exterior.
- El **DTO** define *qué* cruza la frontera de ese servicio.
- La **comunicación** define *cómo* hablan entre sí los servicios.

Estos tres conceptos no son opcionales. Cualquier sistema de microservicios los necesita, y los necesita bien resueltos.

### El repositorio como hilo conductor

- **DtoMappingManual** muestra el mapeo manual entre entidades y DTOs. Es el punto de partida.
- **DtoMappingMapstruct** muestra la misma conversión resuelta con MapStruct. Es la evolución natural.
- **BookingGym-Sync** muestra dos microservicios colaborando por red. Es la aplicación integrada.

### Buenas prácticas

#### API RESTful

- Usar sustantivos en las URLs, verbos en los métodos.
- Devolver códigos de estado HTTP significativos.
- Versionar la API (`/v1/`, `/v2/`).
- Documentar con OpenAPI / Swagger.
- Implementar paginación y filtrado adecuados.
- Manejar errores con un formato consistente.

#### DTOs

- Separar DTOs de entrada y de salida.
- Usar MapStruct para el mapeo.
- No exponer entidades JPA directamente.
- Validar los DTOs de entrada con `@Valid`.

#### Comunicación

- Usar Feign para llamadas síncronas declarativas.
- Usar RabbitMQ o Kafka para asíncrono.
- Configurar timeouts, reintentos y circuit breakers.
- Manejar la idempotencia en consumidores.
- Traducir excepciones técnicas a excepciones de dominio.

### Palabras finales

La arquitectura de microservicios no es una bala de plata. Introduce complejidad distribuida que un monolito no tiene: latencia de red, fallos parciales, consistencia eventual, dificultad de depuración. No tiene sentido adoptarla para un sistema pequeño o para un equipo pequeño.

Pero cuando se adopta, debe hacerse bien. Y hacerlo bien significa entender REST, entender los DTOs, entender los patrones de comunicación. Este libro ha intentado ofrecer una introducción sólida a esos tres pilares, con ejemplos concretos y un recorrido progresivo.

---

## Referencias

- Fielding, R. T. (2000). *Architectural Styles and the Design of Network-based Software Architectures*. Tesis doctoral, University of California, Irvine.
- Richardson, L., & Ruby, S. (2007). *RESTful Web Services*. O'Reilly Media.
- Fowler, M. (2002). *Patterns of Enterprise Application Architecture*. Addison-Wesley.
- Newman, S. (2021). *Building Microservices*. 2ª edición. O'Reilly Media.
- [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [MapStruct](https://mapstruct.org/)
- [Spring AMQP](https://spring.io/projects/spring-amqp)
- [Repositorio de ejemplos](https://github.com/arielhurtado/springbootexamples)
