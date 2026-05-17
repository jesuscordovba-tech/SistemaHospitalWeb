# Sistema Hospital Web

Sistema de gestión hospitalaria integral construido con **Java Jakarta EE** (Servlets + JSP + JDBC) sobre Apache Tomcat 10 con MariaDB.

## Funcionalidades

- **Autenticación** con control de acceso por roles
- **Gestión de usuarios** (CRUD) con 5 roles: Administrador, Médico, Enfermero, Laboratorista, Paciente
- **Gestión de pacientes** (CRUD)
- **Gestión de citas** médicas con asignación de médico y estado
- **Atenciones médicas** con diagnóstico, tratamiento y recetas
- **Signos vitales** (presión arterial, temperatura, frecuencia cardíaca, respiratoria, SpO2)
- **Órdenes de laboratorio** con flujo de estados (Pendiente → Procesando → Completo)
- **Resultados de laboratorio** con valores y unidades
- **Bitácora de auditoría** de todas las operaciones
- **Dashboard** con conteos y estadísticas
- **Vistas filtradas por rol** (médicos ven solo sus pacientes, pacientes ven solo sus datos)

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Jakarta EE (Servlets) | 6.0 |
| Apache Tomcat | 10.1 |
| MariaDB | 10+ |
| JDBC (MariaDB Connector) | 3.3.2 |
| BCrypt | 0.4 |

## Requisitos

- Java 17+
- Apache Tomcat 10.1+
- MariaDB 10+

## Instalación

### 1. Base de datos

```sql
CREATE DATABASE hospital_final;
USE hospital_final;
SOURCE schema.sql;
```

### 2. Configurar conexión

Copia el archivo de configuración de ejemplo y edítalo con tus credenciales:

```bash
cp src/main/webapp/WEB-INF/database.properties.example src/main/webapp/WEB-INF/database.properties
```

Edita `src/main/webapp/WEB-INF/database.properties`:

```properties
db.host=localhost
db.port=3306
db.name=hospital_final
db.user=root
db.password=tu_contraseña
```

### 3. Desplegar en Tomcat

1. Importa el proyecto en Eclipse como "Dynamic Web Project"
2. Asegúrate de que el JAR de BCrypt esté en `WEB-INF/lib/` (ya incluido)
3. Publica en Apache Tomcat 10.1
4. Accede a: `http://localhost:8080/SistemaHospitalWeb`

### Usuarios por defecto

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 1234 | Administrador |
| doc1 | 1234 | Médico |
| enf1 | 1234 | Enfermero |
| lab | 1234 | Laboratorista |
| paciente | 1234 | Paciente |

## Estructura del proyecto

```
src/main/java/
├── config/
│   ├── Conexion.java          -- Conexión a base de datos
│   └── DatabaseConfig.java    -- Configuración desde properties
├── controlador/
│   ├── LoginServlet.java      -- Autenticación
│   ├── LogoutServlet.java     -- Cierre de sesión
│   ├── UsuarioServlet.java    -- CRUD usuarios
│   ├── PacienteServlet.java   -- CRUD pacientes
│   ├── CitaServlet.java       -- CRUD citas
│   ├── AtencionServlet.java   -- CRUD atenciones
│   ├── SignosServlet.java     -- CRUD signos vitales
│   ├── LaboratorioServlet.java -- CRUD laboratorios
│   ├── ResultadoServlet.java  -- CRUD resultados
│   └── BitacoraServlet.java   -- Vista de bitácora
├── modelo/
│   ├── dto/                   -- 8 DTOs
│   └── dao/                   -- 8 DAOs
└── utils/
    └── SecurityUtils.java     -- BCrypt + XSS escape
src/main/webapp/
├── app.jsp                    -- Interfaz completa
└── WEB-INF/
    ├── web.xml
    ├── database.properties
    └── lib/
        ├── mariadb-java-client-3.3.2.jar
        └── jbcrypt-0.4.jar
```
