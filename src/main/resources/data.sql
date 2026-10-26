-- Datos de prueba que se cargan en cada arranque.

INSERT INTO paciente (nombre, email, telefono) VALUES
    ('Ana García', 'ana.garcia@example.com', '600111222'),
    ('Luis Martínez', 'luis.martinez@example.com', '600333444'),
    ('Marta López', 'marta.lopez@example.com', '600555666');

INSERT INTO cita (fecha, motivo, paciente_id) VALUES
    ('2026-11-02', 'Revisión anual', 1),
    ('2026-11-05', 'Vacunación de la gripe', 1),
    ('2026-11-10', 'Consulta de fisioterapia', 2),
    ('2026-11-12', 'Control de tensión', 3),
    ('2026-11-18', 'Revisión de analítica', 2);
