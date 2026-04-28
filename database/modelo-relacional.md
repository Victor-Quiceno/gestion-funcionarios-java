# Modelo relacional - Gestión de funcionarios

## Tabla: cargos

- id_cargo: clave primaria
- nombre_cargo: nombre del cargo

## Tabla: dependencias

- id_dependencia: clave primaria
- nombre_dependencia: nombre de la dependencia

## Tabla: funcionarios

- id_funcionario: clave primaria
- nombres: nombres del funcionario
- apellidos: apellidos del funcionario
- documento: documento único
- correo: correo único
- telefono: teléfono
- fecha_ingreso: fecha de ingreso
- estado: estado del funcionario
- id_cargo: clave foránea hacia cargos
- id_dependencia: clave foránea hacia dependencias

## Relaciones

- Un cargo puede estar asociado a muchos funcionarios.
- Una dependencia puede tener muchos funcionarios.
- Un funcionario pertenece a un cargo.
- Un funcionario pertenece a una dependencia.