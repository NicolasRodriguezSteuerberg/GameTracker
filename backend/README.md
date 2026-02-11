# Game Tracker - Backend API
Este es el núcleo de la aplicación Game Tracker. Se encarga de la gestión de usuarios, persistencia de datos y la sincronización con IGDB.

## Stack Tecnológico
- Lenguaje: Java 21
- Framework: Spring Boot
- Bases De Datos: PostgreSQL
- Seguridad: Spring Security + OAuth2 (Google) + JWT
- Documentación: OpenAPI (Swagger)

## Instalación y Puesta en Marcha

### Prerrequisitos
- Java 21 JDK instalado
- PostgreSQL (local o via Docker)
- Cuenta de desarrollador en [IGDB](https://api-docs.igdb.com/#getting-started) (Twitch Developer).
- Credenciales de Google Cloud Console (OAuth 2.0).

### Pasos
1. **Clonar el repositorio y navegar al backend:**
```shell
git clone https://github.com/NicolasRodriguezSteuerberg/GameTracker.git
cd backend
```
2. **Configurar Variables de Entorno:** Crear un archivo `.env` basándote en el ejemplo:
```env
SPRING_PROFILES_ACTIVE=prod # también está dev

DB_URL=localhost:5432/game_tracker
DB_USER=game_tracker
DB_PASSWORD=game_tracker

JWT_ISSUER=example@gmail.com
ADMIN_EMAILS=example@gmail.com,example2@gmail.com

IGDB_CLIENT_ID=client_id
IGDB_CLIENT_SECRET=client_secret

GOOGLE_CLIENT_ID=google_client_id
GOOGLE_CLIENT_SECRET=google_client_secret
```
3. Ejecutar la aplicación en Linux/Mac
```shell
export $(cat .env | xargs) && ./mvnw spring-boot:run
```

## Documentación API
Una vez levantada la applicación, la documentación interactiva estará disponible en:
- **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

También se puede obtener la especificación OpenAPI en formato JSON:
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Funcionalidades Técnicas
### Autenticación y Seguridad
- **Gogle Login:** Validación de credenciales de Google en el lado del servidor.
- **JWT:** Generación de tokens propias tras el login de Google para independizar la sesión y autorizar peticiones. (Utilización de refresh tokens (por dispositivo) para no tener que volver a iniciar sesión).

### Sincronización con IGDB
El sistema mantiene un catálogo local actualizado para búsquedas rápidas:
1. **Arranque:** Al iniciar el servidor verifica y actualiza el catálogo.
2. **Cron Job:** Tarea programada **todos los días a las 12:00 AM** para buscar actualizaciones o nuevos lanzamientos (siempre que el servidor esté en ejecución).

### Gestión de la Biblioteca Personal
Enpoints protegidos para que cada usuario gestione sus juegos. Permite gestionarlos utilizando /me o el id del usuario
- **CRUD Completo**
- **Filtros:** Consulta tus juegos filtrando por:
    - Estado (Completado, Jugando, Pendiente, Abandonado)
    - Puntuación personal
    - Géneros
    - Plataformas
    - Nombre
    - Tiempo jugado