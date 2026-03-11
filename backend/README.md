# Game Tracker - Backend API
API REST para gestionar bibliotecas de videojuegos con autenticación segura y sincronización con IGDB.

## Quick Start
1. **Clonar el repositorio**
```shell
git clone https://github.com/NicolasRodriguezSteuerberg/GameTracker.git
```
2. **Ir a la carpeta backend**
```shell
cd backend
```
3. **Configurar variables de entorno**
```dotenv
cp .env.example .env
```
4. **Ejecutar docker compose**
```shell
docker build -t gametracker-backend .
docker compose up -d
```
5. **[Abrir Swagger](http://localhost:8080/swagger-ui/index.html)**

## Stack Tecnológico
- Lenguaje: Java 21
- Framework: Spring Boot
- Bases De Datos: PostgreSQL
- Seguridad: Spring Security + OAuth2 (Google) + JWT
- Documentación: OpenAPI (Swagger)

## Arquitectura
La aplicación utiliza una arquitectura monolito modular, esto facilita la escalabilidad y mantenimiento.
Separación de dominios:
- Catálogo
- Biblioteca de usuario
- Autenticación

## Instalación y Configuración

### Prerrequisitos
- Docker y Docker Compose
- Cuenta de desarrollador en [IGDB](https://api-docs.igdb.com/#getting-started) (Twitch Developer).
- Credenciales de Google Cloud Console (OAuth 2.0).

### Configuración de variables de entorno
Copiar el archivo `.env.example` como `.env`

#### Configuración de base de datos
- DB_URL -> dirección de PostgreSQL
- DB_USER -> usuario de la base de datos
- DB_PASSWORD -> contraseña

#### Configuración JWT
- JWT_ISSUER -> identificador del emisor del token
- ADMIN_EMAILS -> correos con permisos administrativos
#### Configuración IGDB
- IGDB_CLIENT_ID -> credencial de Twitch Developer
- IGDB_CLIENT_SECRET -> credencial secreta
#### Configuración Google OAuth
- GOOGLE_CLIENT_ID -> credencial OAuth del proyecto
- GOOGLE_CLIENT_SECRET -> credencial secreta

### Construcción y ejecución con Docker
1. Construir la imagen del backend:
```shell
docker build -t gametracker-backend .
```
2. Levantar el servicio:
```shell
docker compose up -d
```

Esto iniciará:
- La API
- La base de datos PostgreSQL

## Documentación API
Una vez levantada la applicación, la documentación interactiva estará disponible en:
- **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

También se puede obtener la especificación OpenAPI en formato JSON:
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Funcionalidades Técnicas
### Autenticación y Seguridad
- **Google Login:** Validación de credenciales de Google en el lado del servidor.
- **JWT:** Generación de tokens propios tras el login de Google para independizar la sesión y autorizar peticiones. (Utilización de refresh tokens (por dispositivo) para no tener que volver a iniciar sesión).

### Sincronización con IGDB
El sistema mantiene un catálogo local actualizado para búsquedas rápidas:
1. **Arranque:** Al iniciar el servidor verifica y actualiza el catálogo.
2. **Cron Job:** Tarea programada **todos los días a las 12:00 AM** para buscar actualizaciones o nuevos lanzamientos (siempre que el servidor esté en ejecución).

### Gestión de la Biblioteca Personal
Endpoints protegidos para que cada usuario gestione sus juegos. Permite gestionarlos utilizando /me o el id del usuario
- **CRUD Completo**
- **Filtros:** Consulta tus juegos filtrando por:
    - Estado (Completado, Jugando, Pendiente, Abandonado)
    - Puntuación personal
    - Géneros
    - Plataformas
    - Nombre
    - Tiempo jugado

## API Overview
- Catálogo
  - Buscar juegos
  - Obtener detalles
- Biblioteca
  - Añadir juego
  - Actualizar estado
  - Eliminar juego
  - Listar biblioteca
- Autenticación
  - Inicio de sesión
  - Refrescar token
