# Game Tracker
**Game Tracker** es una aplicación para gestionar bibliotecas de videojuegos con filtrado avanzado y sincronizada con IGDB.

## Estado del proyecto
El proyecto está dividido en dos partes:
- **Backend** (terminando)
- **Frontend** (en espera)

## Características principales
- **Sincronización automática:** Conexión directa con IGDB para mantener la información de los juegos siempre actualizada.
- **Seguridad:** Inicio de sesión seguro mediante Google y gestión de sesiones vía JWT.
- **Búsqueda Avanzada:** Encuentra juegos en el catálogo o en tu biblioteca utilizando múltiples filtros.
- **Gestión de Biblioteca:** Añade juegos, califícalos, marca tu progreso (tiempo jugado) y organízalos por estado (jugando, completado, pendiente, abandonado).

## Estructura del proyecto
* `/backend`: Contiene la lógica del servidor, conexión a base de datos y tareas programadas.
* `/frontend`: Interfaz de usuario.

## Tecnologías
- Java & Spring Boot
- PostgreSQL
- Spring Security
- Docker
- IGDB API

## Como probar la API
Para probar la API consulta la documentación en el directorio [backend](/backend/README.md)

## Enlaces
| Recurso | URL Local | Descripción |
| :--- | :--- | :--- |
| **Documentacion** | [Swagger UI](http://localhost:8080/swagger-ui/index.html) | Explorar y probar endpoints |

## Agradecimientos y Atribución

Este proyecto utiliza la API pública de **IGDB** para obtener información sobre videojuegos.
* Datos proporcionados por [IGDB](https://www.igdb.com).
* Iconos y logos propiedad de sus respectivos dueños.

[Powered by IGDB](https://www.igdb.com)
