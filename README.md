# Microservicio de Gestión de Guías Turísticos

Este microservicio gestiona la información y disponibilidad de los guías turísticos para la plataforma de turismo sostenible. Implementa una **arquitectura limpia** con separación de responsabilidades y sigue el patrón **CQRS** (Command Query Responsibility Segregation) con **consistencia eventual** mediante eventos.

## Características Principales

* **Gestión de Guías:** Operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los guías turísticos.
* **Gestión de Disponibilidad:** Permite definir y actualizar la disponibilidad semanal de cada guía, incluyendo franjas horarias específicas.
* **Patrón CQRS:** Separa las operaciones de escritura (comandos) de las de lectura (consultas) para optimizar el rendimiento y la escalabilidad.
    * **Modelo de Escritura:** Utiliza **PostgreSQL** y JPA para persistir los cambios de estado.
    * **Modelo de Lectura:** Utiliza **MongoDB** como base de datos optimizada para consultas.
* **Comunicación Asíncrona:** Emplea **RabbitMQ** para publicar eventos de dominio (Ej: `GuiaCreadoEvent`, `GuiaActualizadoEvent`) cuando ocurren cambios en el modelo de escritura.
* **Sincronización Eventual:** Un listener consume los eventos de RabbitMQ y actualiza el modelo de lectura en MongoDB, asegurando la consistencia eventual entre ambos modelos.
* **Contenerización:** Configurado para ejecutarse en contenedores **Docker** tanto localmente (`docker-compose.yml`) como en producción (`Dockerfile`).
* **Pruebas Robustas:** Incluye una suite completa de tests:
    * **Unitarios:** Para validar la lógica de dominio y casos de uso.
    * **Integración:** Para verificar la interacción con bases de datos (usando **Testcontainers**) y la capa web.
    * **End-to-End (E2E):** Para probar el flujo completo de CQRS, desde la API de comandos hasta la verificación en la API de consultas, pasando por RabbitMQ (usando **Testcontainers** y **Awaitility**).
* **Despliegue Continuo:** Configurado para CI/CD con **GitHub Actions**.
* **Despliegue en la Nube:** Preparado para desplegarse en **Render**.
* **Documentación API:** Integrado con SpringDoc para generar documentación OpenAPI (Swagger).



## Arquitectura

El proyecto sigue los principios de la **Arquitectura Limpia** (Clean Architecture) y el patrón **CQRS**.

* **Domain:** Contiene las entidades (`Guia`, `DisponibilidadDiaria`, etc.), objetos de valor (`GuiaId`, `FranjaHoraria`), eventos de dominio y las interfaces de los repositorios (puertos). Define la lógica de negocio central.
* **Application:** Orquesta los casos de uso (`CreateGuiaUseCase`, `UpdateDisponibilityUseCase`, etc.), implementando los puertos de entrada y utilizando los puertos de salida definidos en el dominio.
* **Infrastructure:** Implementa los adaptadores para interactuar con tecnologías externas:
    * **input:** Controladores REST (`GuiaAdminCommandController`, `GuiaQueryController`), Listener de RabbitMQ (`RabbitMQEventListener`).
    * **output:** Repositorios JPA/Postgres (`PostgresGuiaCommandRepositoryAdapter`), Repositorios Mongo (`MongoGuiaQueryRepositoryAdapter`), Publicador de Eventos RabbitMQ (`RabbitMQEventPublisherAdapter`).
    * **config:** Configuración de Beans, Seguridad, Web, RabbitMQ.

## Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.x
* **Persistencia (Escritura):** Spring Data JPA, Hibernate, PostgreSQL
* **Persistencia (Lectura):** Spring Data MongoDB, MongoDB
* **Mensajería:** Spring AMQP, RabbitMQ
* **API:** Spring Web MVC, Spring Security
* **Pruebas:** JUnit 5, Mockito, Testcontainers, Awaitility
* **Contenerización:** Docker, Docker Compose
* **Build:** Maven
* **CI/CD:** GitHub Actions
* **Despliegue:** Render
* **Documentación API:** SpringDoc (Swagger UI)
* **Otros:** Lombok

## Requisitos Previos

* JDK 17 o superior
* Maven 3.8+
* Docker y Docker Compose (Para ejecución local y tests de integración/E2E)

## Puesta en Marcha Local (Docker Compose)

La forma más sencilla de ejecutar el microservicio junto con sus dependencias (PostgreSQL, MongoDB, RabbitMQ) localmente es usando Docker Compose.

1.  **Clona el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd Microservicio_Guias 
    ```

2.  **Construye y levanta los contenedores:**
    ```bash
    docker-compose up --build -d
    ```
    Esto construirá la imagen de tu aplicación y levantará los contenedores definidos en `docker-compose.yml`.

3.  **Acceso:**
    * **API:** `http://localhost:8080`
    * **Swagger UI:** `http://localhost:8080/swagger-ui.html`
    * **RabbitMQ Management:** `http://localhost:15672` (user: user, pass: password)

4.  **Para detener los contenedores:**
    ```bash
    docker-compose down
    ```

## Ejecución de Pruebas

El proyecto cuenta con una suite completa de pruebas automatizadas.

* **Ejecutar todos los tests (Unitarios, Integración, E2E):**
    Asegúrate de tener Docker corriendo, ya que los tests de Integración y E2E usan Testcontainers.
    ```bash
    ./mvnw clean test 
    ```
    o si tienes Maven instalado globalmente:
    ```bash
    mvn clean test
    ```

* **Ejecutar solo tests unitarios (más rápido):**
    ```bash
    ./mvnw clean test -Dtest="*Test" -DfailIfNoTests=false
    ```

* **Ejecutar solo tests de integración/E2E (requiere Docker):**
    ```bash
    ./mvnw clean test -Dtest="*IT,*E2ETest" -DfailIfNoTests=false
    ```

## API Endpoints

### Endpoints de Comandos (`/admin`)

* **Crear Guía:**
    * `POST /admin`
    * **Body:** `CreateGuiaRequest` (JSON)
        ```json
        {
          "nombre": "Nombre del Guía",
          "email": "email@ejemplo.com",
          "telefono": "123456789"
        }
        ```
    * **Respuesta:** `201 Created` con header `Location`.

* **Actualizar Datos Básicos del Guía:**
    * `PUT /admin/{id}`
    * **Body:** `UpdateGuiaRequest` (JSON)
        ```json
        {
          "nombre": "Nombre Actualizado",
          "email": "nuevo@ejemplo.com",
          "telefono": "987654321"
        }
        ```
    * **Respuesta:** `204 No Content` (éxito), `404 Not Found`.

* **Actualizar Disponibilidad Semanal:**
    * `PUT /admin/{guiaId}/disponibilidad`
    * **Body:** `List<DisponibilidadDiariaRequest>` (JSON)
        ```json
        [
          {
            "dia": "MONDAY",
            "disponible": true,
            "franjas": [
              {"horaInicio": "09:00:00", "horaFin": "12:00:00"},
              {"horaInicio": "14:00:00", "horaFin": "17:00:00"}
            ]
          },
          {
            "dia": "TUESDAY",
            "disponible": false,
            "franjas": []
          } 
          // ... resto de días
        ]
        ```
    * **Respuesta:** `200 OK` (éxito), `404 Not Found`.

* **Eliminar Guía:**
    * `DELETE /admin/{id}`
    * **Respuesta:** `204 No Content`.

### Endpoints de Consulta (`/`)

* **Obtener Todos los Guías:**
    * `GET /`
    * **Respuesta:** `200 OK` con `List<GuiaResponse>` (JSON).

* **Obtener Guía por ID:**
    * `GET /{id}`
    * **Respuesta:** `200 OK` con `GuiaResponse` (JSON), `404 Not Found`.

*(Nota: Los DTOs `DisponibilidadDiariaResponse` y `FranjaHorariaRequest` se usan en las respuestas de consulta y en la petición de actualización de disponibilidad respectivamente)*.

## Despliegue (Render)

Este microservicio está configurado para desplegarse fácilmente en Render utilizando el archivo `render.yaml`.

Render leerá este archivo para:
1.  Construir la imagen Docker usando el `Dockerfile`.
2.  Crear un servicio web basado en esa imagen.
3.  Provisionar una base de datos PostgreSQL gestionada.
4.  Inyectar automáticamente las credenciales de la base de datos como variables de entorno (`SPRING_DATASOURCE_URL`, etc.).

**Importante:** Necesitarás configurar manualmente las variables de entorno para MongoDB Atlas (`SPRING_DATA_MONGODB_URI`) y RabbitMQ (probablemente `SPRING_RABBITMQ_ADDRESSES` si usas un servicio como CloudAMQP) directamente en el dashboard de Render.

## CI/CD (GitHub Actions)

El repositorio incluye un workflow de GitHub Actions (`.github/workflows/ci.yml`) que se activa en cada `push` o `pull_request` a la rama `main`.

Este workflow:
1.  Realiza el checkout del código.
2.  Configura JDK 17.
3.  **Inicia servicios dependientes (PostgreSQL, MongoDB, RabbitMQ) como contenedores dentro de la CI**.
4.  Ejecuta `mvn clean install` pasando el perfil `ci` (`-Dspring.profiles.active=test,ci`) para compilar el código y **correr todos los tests** (unitarios, integración y E2E) conectándose a los servicios de la CI.

Esto asegura que cada cambio es verificado automáticamente antes de integrarse.
