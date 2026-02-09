# Game Tracker
**Game Tracker** es una aplicación diseñada para los entusiastas de los videojuegos que desean llevar un registro detallado de su colección. Permite gestionar qué estás jugando, qué has terminado y qué planeas jugar. El catálogo está sincronizado automáticamente con la base de datos de IGDB.

## Estado del proyecto
El proyecto está dividido en dos partes:
- **Backend** (terminando)
- **Frontend** (en espara)

## Características principales
- **Sincronización automática:** Conexión directa con IGDB para mantener la información de los juegos siempre actulizada.
- **Seguridad:** Inicio de sesión seguro mediante Google y gestión de sesiones vía JWT.
- **Búsqueda Avanzada:** Encuentra juegos en el catálogo o en tu biblioteca utilizando múltiples filtros.
- **Gestión de Biblioteca:** Añade juegos, califícalos, marca tu progreso (tiempo jugado) y organízalos por estado (jugando, completado, pendiente, abandonado).

## Arquitectura
* `/backend`: Contiene la lógica del servidor, conexión a base de datos y tareas programadas.
* `/frontend`: Interfaz de usuario.

## Tecnologías
- **Integraciones:** Google Auth, IGDB API.
- **Autenticación:** JWT (JSON Web Tokens).
- **Base de Datos:** PostgeSQL
- **Backend:** JAVA, Spring Boot


Hecho por Nicolás Rodríguez Steuerberg