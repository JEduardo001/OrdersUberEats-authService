Auth Service
Microservicio responsable de la seguridad del ecosistema, gestionando la autenticación de usuarios mediante JWT y la sincronización de credenciales con el perfil de usuario a través de eventos.

Stack Tecnológico
Java: 21

Framework: Spring Boot 4.0.1

Seguridad: Spring Security + JJWT (JSON Web Token)

Base de Datos: PostgreSQL

Mensajería: Apache Kafka

Service Discovery: Netflix Eureka

API Endpoints
Base Path: /api/v1/auth

POST /login: Autentica credenciales y genera un token JWT con los roles del usuario.

POST /register: Registra una nueva credencial y dispara el flujo de creación de perfil de usuario.

GET /: Obtiene lista paginada de registros de autenticación.

GET /{idAuth}: Obtiene detalle de una credencial por su UUID.

PUT /{idAuth}: Actualiza datos de autenticación existentes.

POST /order: Verifica la identidad del usuario antes de permitir la creación de un pedido.

Comunicación Orientada a Eventos
El servicio utiliza Kafka para mantener la consistencia entre las credenciales de acceso y la información del usuario en otros servicios.

Eventos Consumidos
creating.user.response: Escucha el resultado de la creación del perfil en el User Service para actualizar el estado del usuario (confirmación o rechazo).

Eventos Producidos
[Dynamic Topic]: El productor permite enviar eventos de registro o verificación a tópicos específicos definidos en la lógica de negocio.

failed.send.event.dlq: Canal de Dead Letter Queue para gestionar fallos en el envío de eventos de seguridad.

Seguridad y Trazabilidad
JWT Service: Creación y validación de tokens firmados con una clave secreta configurada.

Authentication Manager: Gestión centralizada de roles y autoridades de Spring Security.

Tracer Correlation: Inserción de correlationId en los headers de Kafka para seguimiento de auditoría en flujos de registro y login.

Configuración de Infraestructura
Puerto de servicio: 5010

Base de Datos: AuthServiceOrderUberEatsDB (Postgres)

Variables Clave: JWT_SECRET_KEY para la firma de tokens.
