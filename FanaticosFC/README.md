# Fanáticos FC  ⚽👕

Diplomado Java UNAM - Proyecto Final

## 🚀 Descripción
Este proyecto consiste en un **Sistema de Gestión de Ventas** para un negocio de playeras de fútbol. 
El sistema permite gestionar el inventario, administrar las ventas y generar reportes sobre estadísticas del negocio.

## 📄 Características Principales
-  🛠️ **CRUD** sobre tablas principales como:
    - Playeras (atributos como talla, color, equipo, marca, etc.)
    - Equipos y marcas relacionados a las playeras
    - Usuarios (Administradores y Usuarios)
    - Clientes
    - Ventas (De contado y a crédito)
    - Pagos

-  📊 **Generación de Reportes en PDF**:
    - Estadísticas mensuales
    - Estadísticas históricas
    - Tickets de ventas

-  .
   📈  **Dashboard de Estadísticas**:
    - Tarjetas que se actualizan cada mes
    - Gráficas históricas 

-  👩‍💻 **Gestión de Usuarios**:
    - Roles de usuario (Administrador y Usuario)
    - Autenticación y autorización mediante Spring Security

- 🌐 **Interfaz Gráfica de Usuario**:
    - Desarrollada con HTML, Thymeleaf, CSS y Bootstrap para un diseño responsivo y moderno.

##  ⚙️ Tecnologías Utilizadas
- **Backend**: Java con Spring Boot 3
- **Frontend**: Thymeleaf y Bootstrap
- **Base de Datos**: MariaDB
- 🔐 **Seguridad**: Spring Security
- **Autenticación con JWT y Cookies**:
    - Uso de JSON Web Tokens (JWT) para autenticación segura.
    - Almacenamiento de JWT en cookies para facilitar el manejo de sesiones y aumentar la seguridad.
    - Cifrado de contraseñas mediante el algoritmo BCrypt.
- **Generación de Reportes**: OpenPDF para generar los PDF's
- **Gráficos**: Chart.js para representaciones visuales.

## 🔧 Configuración entorno de desarrollo
### Versiones Recomendadas
| Recomendado              | Reference                                                              | Notes                                                                            |
|--------------------------|------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| Oracle Java 17 JDK       | [Descarga](https://www.oracle.com/java/technologies/downloads/#java17) | Java 17 o superior es requerido para SpringBoot 3                                |
| IntelliJ 2022 o superior | [Descarga](https://www.jetbrains.com/idea/download/)                   | Edición Ultimate recomendada, Sin embargo también funciona en Edición Comunidad. |
| Maven 3.9.0 o superior   | [Descarga](https://maven.apache.org/download.cgi)                      |                                                                                  |

## 💻 Uso del Sistema

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/RoyMartinez3103/FanaticosFC_PF.git
   ```
2. Configurar la base de datos MariaDB:
    - Ejecutar el script `BD_Fanaticos.sql`
    - Configurar las credenciales en el archivo `application.properties` en los apartados:
      - `spring.datasource.username=..`
        `spring.datasource.password=..`

3. Colocar la carpeta `uploads` en  C:\ . ❗❗
   - Esta carpeta contiene las imagenes de las playeras y será donde se guarden cuando el usuario registre una playera.

4. Ejecutar la aplicación en IntelliJ. 

5. Acceder al sistema desde el navegador en:
      ```
      http://localhost:8080/fanaticosfc/login
      ```
    Para efectos prácticos se proporcionan los siguientes usuarios:
   - ADMIN: 
     - username: ana_garcia
     - password: contrasena123
   - USER:
     - username: juan_perez
     - password: contrasena456

## 👀 Información Adicional
- **Roles y Acceso**:
    - Administrador: 
      - Gestión completa de inventario, ventas y usuarios.
      - Posibilidad de ver estadísticas.
    - Empleado: 
      - Registro de ventas y registro de pagos.
- **Gestión de Playeras**:
    - Crear, ver, actualizar y eliminar registros.
- **Realizar ventas:**:
  - De contado
  - A crédito
    - Asignar a un cliente
    - Registrar pagos
- **Estadísticas**:
    - Visualizar tendencias de venta y playeras populares.
    - Así como los usuarios que más venden.

## 👦 Autor
- [@Rodrigo Martínez Zambrano](https://github.com/RoyMartinez3103)


