# Centro de Gestion de Consultas Medicas

Aplicacion web desarrollada con Spring Boot para administrar consultas medicas con autenticacion, autorizacion por roles, validaciones en frontend y backend, envio de datos en JSON desde JavaScript y documentacion de servicios con Swagger.

## Tecnologias

- Java 21
- Spring Boot 4.0.0
- Spring Web
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Lombok
- Bootstrap 5
- Swagger / OpenAPI con Springdoc

## Roles del sistema

La aplicacion maneja tres roles:

- `ADMINISTRADOR`: puede crear, editar y eliminar medicos. Tambien registra consultas medicas, asigna consultorios y asigna medicos.
- `MEDICO`: puede visualizar unicamente las consultas asignadas a su usuario.
- `PACIENTE`: puede visualizar sus citas y actualizar solo el horario de las citas que tiene asignadas.

Si un usuario intenta ingresar a una vista o endpoint sin permisos, el sistema muestra una vista personalizada de acceso restringido.

## Estructura principal

```text
src/main/java/usco/edu/co/Parcial2ConsultasMedicas
├── config          Configuracion de seguridad y Swagger
├── controller      Controladores web y REST
├── dto             Objetos de entrada para JSON/formularios
├── model           Entidades JPA
├── repository      Repositorios Spring Data JPA
└── service         Logica de negocio y validaciones

src/main/resources
├── static/css      Estilos y JavaScript
├── templates       Vistas Thymeleaf
└── application.properties

scripts
└── database.sql    Datos iniciales para PostgreSQL
```

## Base de datos

La base de datos usada por defecto se llama:

```text
consultas_medicas
```

La configuracion esta en:

```text
src/main/resources/application.properties
```

Valores actuales:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/consultas_medicas
spring.datasource.username=postgres
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
```

Si tu usuario o clave de PostgreSQL son diferentes, cambia esos valores antes de ejecutar la aplicacion.

## Modelo normalizado

El sistema separa la informacion en tablas independientes:

- `roles`: roles disponibles.
- `users`: credenciales de acceso.
- `users_roles`: relacion entre usuarios y roles.
- `medico`: datos propios del medico.
- `paciente`: datos propios del paciente.
- `consultorio`: numeros de consultorio disponibles.
- `consulta`: citas medicas registradas.

Los nombres y apellidos de medicos y pacientes se guardan en columnas separadas para evitar datos mezclados. El nombre completo se arma solo para mostrarlo en pantalla.

## Instalacion y ejecucion

1. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE consultas_medicas;
```

2. Entrar a la carpeta del proyecto:

```powershell
cd C:\Users\Asus\Downloads\Parcial2ConsultasMedicas\Parcial2ConsultasMedicas
```

3. Compilar el proyecto:



4. Ejecutar la aplicacion:



5. Abrir en el navegador:

```text
http://localhost:8080
```

6. Despues de ejecutar la aplicacion una vez, correr el script:

```text
INSERT INTO roles (name)
VALUES
    ('ADMINISTRADOR'),
    ('MEDICO'),
    ('PACIENTE')
ON CONFLICT (name) DO NOTHING;

-- Usuarios base:
-- administrador / 1234
-- medicos: anagomez, carlosruiz, lauramora / clave igual al usuario
-- pacientes: juanperez, sofiacastro, andreslopez / clave igual al usuario
INSERT INTO users (username, password, enabled)
VALUES
    ('administrador', '{noop}1234', true),
    ('anagomez', '{noop}anagomez', true),
    ('carlosruiz', '{noop}carlosruiz', true),
    ('lauramora', '{noop}lauramora', true),
    ('juanperez', '{noop}juanperez', true),
    ('sofiacastro', '{noop}sofiacastro', true),
    ('andreslopez', '{noop}andreslopez', true)
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    enabled = EXCLUDED.enabled;

INSERT INTO users_roles (user_id, role_id)
VALUES
    ((SELECT user_id FROM users WHERE username = 'administrador'), (SELECT role_id FROM roles WHERE name = 'ADMINISTRADOR')),
    ((SELECT user_id FROM users WHERE username = 'anagomez'), (SELECT role_id FROM roles WHERE name = 'MEDICO')),
    ((SELECT user_id FROM users WHERE username = 'carlosruiz'), (SELECT role_id FROM roles WHERE name = 'MEDICO')),
    ((SELECT user_id FROM users WHERE username = 'lauramora'), (SELECT role_id FROM roles WHERE name = 'MEDICO')),
    ((SELECT user_id FROM users WHERE username = 'juanperez'), (SELECT role_id FROM roles WHERE name = 'PACIENTE')),
    ((SELECT user_id FROM users WHERE username = 'sofiacastro'), (SELECT role_id FROM roles WHERE name = 'PACIENTE')),
    ((SELECT user_id FROM users WHERE username = 'andreslopez'), (SELECT role_id FROM roles WHERE name = 'PACIENTE'))
ON CONFLICT DO NOTHING;

INSERT INTO medico (nombre, apellido, especialidad, usuario_id)
VALUES
    ('Ana', 'Gomez', 'Medicina general', (SELECT user_id FROM users WHERE username = 'anagomez')),
    ('Carlos', 'Ruiz', 'Pediatria', (SELECT user_id FROM users WHERE username = 'carlosruiz')),
    ('Laura', 'Mora', 'Dermatologia', (SELECT user_id FROM users WHERE username = 'lauramora'))
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO paciente (nombre, apellido, documento, usuario_id)
VALUES
    ('Juan', 'Perez', '1076905343', (SELECT user_id FROM users WHERE username = 'juanperez')),
    ('Sofia', 'Castro', '1077729493', (SELECT user_id FROM users WHERE username = 'sofiacastro')),
    ('Andres', 'Lopez', '1080123456', (SELECT user_id FROM users WHERE username = 'andreslopez'))
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO consultorio (numero)
VALUES
    (1), (2), (3), (4), (5),
    (6), (7), (8), (9), (10),
    (11), (12), (13), (14), (15),
    (16), (17), (18), (19), (20)
ON CONFLICT (numero) DO NOTHING;

INSERT INTO consulta (nombre_paciente, motivo_consulta, consultorio_id, hora_inicio, hora_fin, medico_id, paciente_id)
VALUES
    ('Juan Perez', 'Control general', (SELECT id FROM consultorio WHERE numero = 1), '07:00:00', '08:00:00',
     (SELECT id FROM medico WHERE nombre = 'Ana' AND apellido = 'Gomez'), (SELECT id FROM paciente WHERE documento = '1076905343')),
    ('Sofia Castro', 'Dolor de garganta', (SELECT id FROM consultorio WHERE numero = 2), '08:00:00', '09:00:00',
     (SELECT id FROM medico WHERE nombre = 'Carlos' AND apellido = 'Ruiz'), (SELECT id FROM paciente WHERE documento = '1077729493')),
    ('Andres Lopez', 'Revision de piel', (SELECT id FROM consultorio WHERE numero = 3), '09:00:00', '10:00:00',
     (SELECT id FROM medico WHERE nombre = 'Laura' AND apellido = 'Mora'), (SELECT id FROM paciente WHERE documento = '1080123456'));
```

Ese script crea roles, usuarios de prueba, medicos, pacientes, consultorios y consultas iniciales.

## Usuarios de prueba

### Administrador

```text
Usuario: administrador
Clave: 1234
```

### Medicos

```text
Usuario: anagomez
Clave: anagomez

Usuario: carlosruiz
Clave: carlosruiz

Usuario: lauramora
Clave: lauramora
```

### Pacientes

```text
Usuario: juanperez
Clave: juanperez

Usuario: sofiacastro
Clave: sofiacastro

Usuario: andreslopez
Clave: andreslopez
```

## Flujo de uso

### Registro de paciente

Desde el login existe un enlace para registrarse como paciente:

```text
http://localhost:8080/registro
```

El formulario solicita:

- Nombres
- Apellidos
- Documento
- Usuario
- Contrasena

Si un campo esta vacio o tiene formato incorrecto, el error aparece debajo del campo correspondiente.

### Administrador

Al iniciar sesion como administrador se abre:

```text
http://localhost:8080/administrador
```

Desde este panel se puede:

- Crear consultas medicas.
- Editar consultas medicas.
- Eliminar consultas medicas.
- Crear medicos.
- Editar medicos.
- Eliminar medicos.
- Abrir Swagger.

Para crear una consulta:

1. Buscar el paciente por documento con el boton de lupa.
2. Confirmar que aparezca el mensaje `Se encontro`.
3. Seleccionar consultorio desde la lista desplegable de 1 a 20.
4. Seleccionar medico.
5. Escribir motivo de consulta.
6. Seleccionar hora inicio y hora fin.
7. Guardar.

El sistema valida que:

- El paciente exista.
- El consultorio este entre 1 y 20.
- La hora final sea mayor que la hora inicial.
- El medico no tenga otra consulta cruzada.
- El consultorio no tenga otra consulta cruzada.

Para crear un medico:

1. Abrir `Crear Medico`.
2. Escribir nombres y apellidos separados.
3. Seleccionar especialidad desde la lista desplegable.
4. Escribir usuario.
5. Escribir contrasena.
6. Guardar.

### Medico

Al iniciar sesion como medico se abre:

```text
http://localhost:8080/medico
```

El medico solo puede visualizar las consultas que tiene asignadas. No puede crear, editar ni eliminar consultas.

### Paciente

Al iniciar sesion como paciente se abre:

```text
http://localhost:8080/paciente
```

El paciente puede:

- Ver sus citas asignadas.
- Actualizar el horario de sus propias citas.

No puede actualizar citas de otros pacientes.

## Validaciones

La aplicacion tiene validaciones en dos niveles:

### Frontend

Archivo:

```text
src/main/resources/static/css/js/app.js
```

Incluye expresiones regulares para:

- Nombres
- Apellidos
- Documento
- Usuario
- Especialidad
- Motivo de consulta
- Horarios
- Consultorios

Los formularios envian datos al backend usando `fetch` con JSON.

### Backend

Los DTOs usan anotaciones como:

- `@NotBlank`
- `@NotNull`
- `@Size`
- `@Pattern`

Ademas, los servicios validan reglas de negocio como cruces de horario y permisos sobre citas asignadas.

## Swagger

La documentacion de endpoints esta disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

Los DTOs tienen ejemplos definidos para evitar datos aleatorios en Swagger.

Ejemplo de crear medico:

```json
{
  "nombre": "Ana",
  "apellido": "Gomez",
  "especialidad": "Medicina general",
  "username": "anagomez",
  "password": "anagomez"
}
```

Ejemplo de crear consulta:

```json
{
  "nombrePaciente": "Juan Perez",
  "motivoConsulta": "Control general",
  "numeroConsultorio": 1,
  "horaInicio": "07:00",
  "horaFin": "08:00",
  "medicoId": 1,
  "pacienteId": 1
}
```

## Endpoints principales

### Consultas

```text
GET    /api/consultas
POST   /api/consultas
PUT    /api/consultas/{id}
DELETE /api/consultas/{id}
GET    /api/consultas/mis-medico
GET    /api/consultas/mis-paciente
PUT    /api/consultas/{id}/horario
```

### Medicos

```text
GET    /api/medicos
POST   /api/medicos
PUT    /api/medicos/{id}
DELETE /api/medicos/{id}
```

### Pacientes

```text
GET /api/pacientes/buscar?documento=1076905343
```

## Notas importantes

- Si cambias entidades y ya existe una base con datos viejos, puede ser mejor borrar y recrear la base para evitar columnas antiguas o datos incompletos.
- Si Swagger muestra ejemplos viejos, recarga con `Ctrl + F5`.
- El script `database.sql` debe ejecutarse despues de que JPA cree las tablas.
- El proyecto usa Lombok para evitar escribir getters y setters. Los metodos calculados como `getNombreCompleto()` y `getNumeroConsultorio()` se mantienen manualmente porque no son campos directos.


