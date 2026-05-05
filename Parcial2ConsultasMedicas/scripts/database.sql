-- PostgreSQL
-- Crear la base antes de ejecutar la aplicacion:
-- CREATE DATABASE consultas_medicas;

-- La aplicacion crea las tablas con JPA:
-- spring.jpa.hibernate.ddl-auto=update
-- Ejecute primero la app una vez y luego corra estos inserts.
-- Estructura normalizada: roles, usuarios, medicos, pacientes, consultorios y consultas son tablas independientes.

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

INSERT INTO medico (nombre, especialidad, usuario_id)
VALUES
    ('Ana Gomez', 'Medicina general', (SELECT user_id FROM users WHERE username = 'anagomez')),
    ('Carlos Ruiz', 'Pediatria', (SELECT user_id FROM users WHERE username = 'carlosruiz')),
    ('Laura Mora', 'Dermatologia', (SELECT user_id FROM users WHERE username = 'lauramora'))
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO paciente (nombre, documento, usuario_id)
VALUES
    ('Juan Perez', '1076905343', (SELECT user_id FROM users WHERE username = 'juanperez')),
    ('Sofia Castro', '1077729493', (SELECT user_id FROM users WHERE username = 'sofiacastro')),
    ('Andres Lopez', '1080123456', (SELECT user_id FROM users WHERE username = 'andreslopez'))
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
     (SELECT id FROM medico WHERE nombre = 'Ana Gomez'), (SELECT id FROM paciente WHERE nombre = 'Juan Perez')),
    ('Sofia Castro', 'Dolor de garganta', (SELECT id FROM consultorio WHERE numero = 2), '08:00:00', '09:00:00',
     (SELECT id FROM medico WHERE nombre = 'Carlos Ruiz'), (SELECT id FROM paciente WHERE nombre = 'Sofia Castro')),
    ('Andres Lopez', 'Revision de piel', (SELECT id FROM consultorio WHERE numero = 3), '09:00:00', '10:00:00',
     (SELECT id FROM medico WHERE nombre = 'Laura Mora'), (SELECT id FROM paciente WHERE nombre = 'Andres Lopez'));
