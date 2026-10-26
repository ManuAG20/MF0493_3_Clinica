-- Esquema de la base de datos de la clínica.
-- Relación 1:N: un paciente puede tener muchas citas.

DROP TABLE IF EXISTS cita;
DROP TABLE IF EXISTS paciente;

CREATE TABLE paciente (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    email    VARCHAR(120) NOT NULL,
    telefono VARCHAR(30)
);

CREATE TABLE cita (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha       DATE         NOT NULL,
    motivo      VARCHAR(200) NOT NULL,
    paciente_id BIGINT       NOT NULL,
    CONSTRAINT fk_cita_paciente
        FOREIGN KEY (paciente_id) REFERENCES paciente (id)
);
