# Sistema de Gestión de Funcionarios (Desarrollo Seguro)

Este proyecto es una aplicación de escritorio desarrollada en Java Swing orientada a demostrar buenas prácticas de **Desarrollo Seguro**. Implementa un CRUD completo para una tabla de "Funcionarios", manejando relaciones con "Cargos" y "Dependencias".

El enfoque principal del proyecto es la seguridad, incorporando:
- **Prevención de Inyección SQL:** Uso estricto de `PreparedStatement`.
- **Patrón DAO:** Separación total entre la vista, la lógica de negocio y el acceso a datos.
- **Manejo Seguro de Excepciones:** Ocultamiento de trazas de error del motor de base de datos al usuario final.
- **Protección de Credenciales:** Uso de variables de entorno (`.env`) para evitar contraseñas *hardcoded* en el código.
- **Plug and Play:** El código incluye un inicializador automático que construye la base de datos si esta no existe.

---

## Requisitos Previos

Para ejecutar este proyecto en tu propia máquina, necesitas tener instalado:
1. **Java JDK 21** (o superior).
2. **Maven** (Para la gestión de dependencias).
3. **PostgreSQL** (El motor de base de datos).
4. **Git** (Opcional, para clonar el repositorio).

---

## Guía de Ejecución (Paso a Paso)

Sigue estas instrucciones al pie de la letra para ejecutar la aplicación con éxito.

### Paso 1: Configurar PostgreSQL
No necesita crear la base de datos manualmente, pero sí necesita tener el motor encendido.
1. Si no tiene PostgreSQL, descárguelo e instálelo.
2. Durante la instalación, le pedirá que asigne una **contraseña** para el usuario administrador (`postgres`). Escriba una contraseña que recuerde.
3. Puede ignorar herramientas adicionales como "Stack Builder".
4. Asegúrese de que el servicio de PostgreSQL esté corriendo en su sistema (suele iniciar automáticamente en el puerto `5432`).

### Paso 2: Clonar y Preparar el Entorno
1. Clone este repositorio o descargue el archivo `.zip` y descomprímalo.
2. Abra la carpeta del proyecto.
3. Busca el archivo llamado **`.env.example`** y haz una copia de él.
4. Renombra esa copia exactamente a **`.env`** (asegúrate de que no tenga extensión `.txt` oculta).

### Paso 3: Configurar las Credenciales
1. Abre tu nuevo archivo `.env` con un editor de texto (Bloc de notas, VSCode, etc).
2. Modifica la variable `DB_PASSWORD` poniendo la contraseña real que le pusiste a PostgreSQL en el Paso 1.
3. Si durante la instalación de PostgreSQL cambiaste el puerto por defecto (5432), asegúrate de cambiar ese número en la línea de `DB_URL`.

Debería verse algo así:
```env
DB_URL=jdbc:postgresql://localhost:5432/gestion_funcionarios
DB_USER=postgres
DB_PASSWORD=MiContrasena123
```

### Paso 4: Compilar y Ejecutar
Abre una terminal (CMD, PowerShell o la terminal de tu IDE) ubicada en la carpeta raíz del proyecto (donde está el archivo `pom.xml`) y ejecuta el siguiente comando para que Maven descargue las dependencias y compile el código:

```bash
mvn clean compile
```

Una vez compile correctamente, levanta la aplicación visual con este comando:

```bash
mvn exec:java -Dexec.mainClass="view.FrmFuncionario"
```

### ¿Qué sucederá a continuación?
- Si es la primera vez que lo corres, la consola mostrará que **creó la base de datos automáticamente**, construyó las tablas insertó datos de prueba (Cargos y Dependencias).
- Inmediatamente después, se abrirá la **Interfaz Gráfica (Java Swing)** y podrás interactuar con el sistema sin errores.

---

## Estructura del Proyecto

- `src/main/java/config`: Configuración de BD, lectura de `.env` e inicializador.
- `src/main/java/dao`: Interfaces y lógica de conexión PostgreSQL.
- `src/main/java/exception`: Excepciones personalizadas.
- `src/main/java/model`: POJOs (Entidades).
- `src/main/java/view`: Interfaz gráfica (Swing).
- `src/main/resources/database`: Scripts SQL puros (usados por el autoinstalador).
