\# ReservaMS - Notification Service



\## Descripcion



Este microservicio administra las notificaciones del sistema.



Permite crear notificaciones para usuarios y cambiar su estado a enviada o fallida.



\## Responsabilidades



\- Crear notificaciones.

\- Listar notificaciones.

\- Buscar notificaciones por usuario.

\- Buscar notificaciones por estado.

\- Buscar notificaciones por tipo.

\- Marcar notificaciones como enviadas.

\- Marcar notificaciones como fallidas.



\## Puerto



8088



\## Base de datos



reservams\_notification\_db



\## Endpoints principales



\- GET /api/v1/notifications

\- GET /api/v1/notifications/{id}

\- GET /api/v1/notifications/user/{userId}

\- GET /api/v1/notifications/status/{status}

\- GET /api/v1/notifications/type/{type}

\- POST /api/v1/notifications

\- PUT /api/v1/notifications/{id}/sent

\- PUT /api/v1/notifications/{id}/failed



\## Ejecucion



1\. Crear la base de datos reservams\_notification\_db.

2\. Ejecutar el script SQL ubicado en la carpeta database.

3\. Levantar Eureka Server.

4\. Ejecutar el notification-service.

5\. Probar los endpoints desde Postman o desde el API Gateway.



