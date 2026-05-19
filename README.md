# 🚀 PromoHUB SGO - API & Data Pipeline

> **Sistema centralizado para la gestión y consulta de promociones bancarias en Santiago del Estero.**

Este proyecto combina una arquitectura **Backend robusta en Java (Spring Boot)** con un sistema de **automatización de datos en Python**. El objetivo es scrapear, procesar y exponer promociones financieras (bancos, tarjetas locales) a través de una API REST y una interfaz web amigable.

---

## 🏗️ Arquitectura del Proyecto

El sistema se divide en dos módulos principales:

1. **Core Backend (Java):** API RESTful y Web App que gestiona la base de datos y la visualización.
2. **Data Engineering (Python):** Scripts ETL (Extract, Transform, Load) que obtienen los datos de las webs oficiales y alimentan al backend.

---

## ☕ Módulo Backend (Java + Spring Boot)

Aplicación construida bajo el patrón MVC y N-Capas para asegurar escalabilidad y mantenimiento.

### 🛠️ Tecnologías
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3
* **Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** MySQL (XAMPP)
* **Frontend:** Thymeleaf + Bootstrap 5
* **Validaciones:** Jakarta Validation (Bean Validation)

### ✨ Funcionalidades Clave
* **API RESTful:** Endpoints para crear, listar y filtrar promociones.
* **Paginación del Lado del Servidor:** Implementación eficiente con `Pageable` para manejar grandes volúmenes de datos.
* **Filtros Dinámicos:** Búsqueda por categorías, vigencia y texto libre con persistencia de estado entre páginas.
* **Validación de Datos:** Control estricto de inputs mediante DTOs y anotaciones `@Valid`.

### 🧠 Decisiones de Arquitectura y Patrones
* **Patrón Upsert Pragmático:** El endpoint de ingesta (`POST /api/promociones`) está optimizado para su principal cliente (el Scraper en Python). Utiliza una **Clave Natural** (Entidad + Título) para evaluar la persistencia. En lugar de lanzar errores `409 Conflict` ante registros existentes, delega en Hibernate la actualización (`UPDATE`) o creación (`INSERT`).
* **API Orientada al Cliente:** El controlador unifica la respuesta en un `200 OK`, lo que elimina peticiones dobles de verificación por parte del bot, reduciendo la latencia general del sistema.
* **Testing Estricto:** Suite de pruebas desarrollada con JUnit 5 y Mockito, garantizando la ausencia de falsos positivos y manteniendo el motor de mocks libre de *Unnecessary Stubs* (código muerto) tras cada refactorización del negocio.

---

## 🐍 Módulo de Automatización (Python)

Pipeline de datos encargado de mantener la información actualizada automáticamente.

### 🛠️ Tecnologías (Librerías)
* **Selenium:** Navegación automatizada en sitios dinámicos (SPA).
* **BeautifulSoup4:** Parsing y extracción de HTML de alta velocidad.
* **Requests:** Comunicación HTTP para enviar datos a la API Java.
* **Regex & Datetime:** Normalización y limpieza de textos y fechas.

### 🔄 Flujo de Datos (Pipeline)

1. **Extracción (`main.py`):**
   * Inicia un navegador Chrome controlado por software.
   * Navega a los sitios de las entidades financieras (ej. Tarjeta Sol).
   * Extrae el HTML crudo renderizado (manejando tiempos de carga JS).
   * Genera un archivo intermedio: `promociones_sol.json`.

2. **Transformación (`procesador.py`):**
   * Lee los datos crudos.
   * **Limpieza:** Normaliza espacios y caracteres especiales.
   * **Fechas:** Convierte formatos de texto (ej: "12-05-2025") a objetos fecha SQL (`YYYY-MM-DD`).
   * **Categorización:** Separa lógica de "Comercios Adheridos" vs "Legales/Términos".
   * Genera dataset limpio: `promociones_procesadas.json`.

3. **Carga (`cargar_datos.py`):**
   * Lee el dataset limpio e itera sobre cada promoción.
   * Realiza peticiones **HTTP POST** al backend Java (`http://localhost:8080/api/promociones`), simulando ser un cliente externo.

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
* Java JDK 17+
* Python 3.8+
* Google Chrome (para Selenium)

### ⚙️ Configuración del Entorno Local

Este proyecto utiliza variables de entorno para proteger las credenciales de la base de datos.
Antes de ejecutar la aplicación, debes configurar las siguientes variables en tu entorno de desarrollo (por ejemplo, en las *Run/Debug Configurations* de IntelliJ):

| Variable de Entorno | Descripción | Valor de Ejemplo |
| :--- | :--- | :--- |
| `DB_URL` | URL de conexión a la base de datos MySQL | `jdbc:mysql://localhost:3306/tu_base_de_datos?serverTimezone=UTC` |
| `DB_USER_NAME` | Usuario de la base de datos | `admin` |
| `DB_PASSWORD` | Contraseña de la base de datos | *(Tu contraseña local o dejar en blanco)* |

### 1. Levantar el Backend
```bash
# Clonar el repositorio
git clone [https://github.com/TU_USUARIO/PromosHubSGO.git](https://github.com/TU_USUARIO/PromosHubSGO.git)

# Ejecutar con Maven (o desde NetBeans/IntelliJ/Eclipse)
./mvnw spring-boot:run
