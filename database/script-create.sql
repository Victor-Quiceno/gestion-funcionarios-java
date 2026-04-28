CREATE TABLE cargos (
    id_cargo SERIAL PRIMARY KEY,
    nombre_cargo VARCHAR(100) NOT NULL
);

CREATE TABLE dependencias (
    id_dependencia SERIAL PRIMARY KEY,
    nombre_dependencia VARCHAR(100) NOT NULL
);

CREATE TABLE funcionarios (
    id_funcionario SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    fecha_ingreso DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    id_cargo INT NOT NULL,
    id_dependencia INT NOT NULL,
    CONSTRAINT fk_funcionario_cargo
        FOREIGN KEY (id_cargo)
        REFERENCES cargos(id_cargo),
    CONSTRAINT fk_funcionario_dependencia
        FOREIGN KEY (id_dependencia)
        REFERENCES dependencias(id_dependencia)
);