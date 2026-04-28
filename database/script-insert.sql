INSERT INTO cargos (nombre_cargo) VALUES
('Analista'),
('Coordinador'),
('Auxiliar');

INSERT INTO dependencias (nombre_dependencia) VALUES
('Talento Humano'),
('Sistemas'),
('Contabilidad');

INSERT INTO funcionarios (
    nombres,
    apellidos,
    documento,
    correo,
    telefono,
    fecha_ingreso,
    estado,
    id_cargo,
    id_dependencia
) VALUES
('Carlos', 'Ramírez', '1001001001', 'carlos.ramirez@entidad.com', '3001234567', '2024-01-15', 'Activo', 1, 2),
('Laura', 'Gómez', '1001001002', 'laura.gomez@entidad.com', '3019876543', '2023-08-10', 'Activo', 2, 1),
('Andrés', 'Pérez', '1001001003', 'andres.perez@entidad.com', '3024567890', '2022-05-20', 'Inactivo', 3, 3);