# ThinkUs_BTG_Pactua

La solución propuesta consiste en el desarrollo de un sistema backend basado en arquitectura de microservicios, implementado con Spring Boot, que permite gestionar suscripciones, cancelaciones y consultas de historial de transacciones, integrando además servicios de notificación y persistencia de datos.

🧩 Arquitectura

Se adoptó una arquitectura desacoplada y escalable, compuesta por los siguientes componentes:

API REST: expone los endpoints para la gestión de suscripciones, cancelaciones y consultas.

Capa de servicios: contiene la lógica de negocio, validaciones y orquestación de procesos.

Capa de persistencia: maneja el acceso a datos mediante repositorios.

Módulo de notificaciones: encargado del envío de mensajes vía email y SMS.

Manejo centralizado de excepciones: implementación de controladores globales (@ControllerAdvice) para respuestas consistentes.

Tecnologías Utilizadas

Java 11+ / Spring Boot

Docker para contenerización y despliegue

PostgreSQL para almacenamiento relacional

MongoDB (opcional) para manejo de logs o historial flexible

JUnit / Mockito para pruebas

Flyway o Liquibase para control de versiones de base de datos

🔄 Funcionalidades Implementadas
1. Gestión de Suscripciones

Creación de nuevas suscripciones asociadas a clientes y productos.

Validación de disponibilidad por sucursal.

Registro de fechas y estado de la suscripción.

2. Cancelación de Suscripciones

Cancelación lógica (soft delete o cambio de estado).

Registro de motivo y fecha de cancelación.

3. Historial de Transacciones

Consulta detallada de operaciones realizadas por cliente.

Almacenamiento estructurado para auditoría.

4. Notificaciones (Email/SMS)

Envío automático de notificaciones al crear o cancelar suscripciones.

Integración desacoplada mediante interfaces para permitir múltiples proveedores.


## Qué resuelve

- Creación de clientes con saldo inicial de **COP 500.000**
- Consulta de fondos parametrizados
- Suscripción a fondos con validación de saldo disponible
- Cancelación de suscripción y devolución del saldo
- Historial de transacciones por cliente
- Notificación simulada por **EMAIL** o **SMS**
- Manejo global de errores
- Swagger/OpenAPI
- Datos semilla de los 5 fondos del enunciado

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Validation
- Spring Data MongoDB
- Docker / Docker Compose
- JUnit 5 / Mockito

## Estructura

- `controller`: endpoints REST
- `application.service`: lógica de negocio
- `infrastructure.persistence.mongo`: documentos y repositorios MongoDB
- `infrastructure.notification`: notificación desacoplada
- `infrastructure.exception`: manejo global de errores

## Requisitos

- Java 17
- Maven 3.9+
- Docker y Docker Compose

## Ejecución local

```bash
mvn spring-boot:run
```

## Ejecución con Docker

```bash
docker compose up --build
```

## Endpoints

### Fondos
- `GET /api/v1/funds`

### Clientes
- `POST /api/v1/clients`
- `GET /api/v1/clients/{clientId}`
- `GET /api/v1/clients/{clientId}/balance`

### Suscripciones
- `POST /api/v1/subscriptions`
- `POST /api/v1/subscriptions/{subscriptionId}/cancel`
- `GET /api/v1/clients/{clientId}/subscriptions`

### Transacciones
- `GET /api/v1/clients/{clientId}/transactions`

## Ejemplo de creación de cliente

```json
{
  "name": "Christian Valencia",
  "email": "christian@example.com",
  "phone": "3001234567",
  "notificationPreference": "EMAIL"
}
```

## Ejemplo de suscripción

```json
{
  "clientId": "ID_DEL_CLIENTE",
  "fundId": 1
}
```

## Reglas clave implementadas

- un cliente solo puede tener **una suscripción activa por fondo**
- si no hay saldo suficiente, se responde con:
  `No tiene saldo disponible para vincularse al fondo <nombre>`
- la cancelación retorna el monto completo al saldo del cliente
- cada transacción genera un identificador único de negocio

## Swagger

Con la aplicación arriba:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`