package dao;

import config.DatabaseConfig;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Clase base para la conexión a la base de datos.
//Las clases de implementación DAO (como FuncionarioImpl) heredan de esta clase.
public class Conexion {

    // El objeto Connection de java.sql. Es "protected" para que las clases hijas
    // puedan usarlo dentro de su paquete, nada mas.
    protected Connection conexion;

    // Método para esetablecer la conexión con la base de datos.
    // Retorna el objeto Connection para poder usarlo con try-with-resources.
    public Connection conectar() throws DatabaseException {
        try {
            // Primero se carga el driver para que Java sepa como hablar con la base de
            // datos
            // Esto debe ir ANTES de intentar conectar.
            Class.forName(DatabaseConfig.JDBC_DRIVER);

            // Segundo se crea la conexión con la base de datos, usando las constantes de
            // DatabaseConfig.
            conexion = DriverManager.getConnection(
                    DatabaseConfig.DB_URL,
                    DatabaseConfig.USER,
                    DatabaseConfig.PASSWORD);

            return conexion;

        } catch (ClassNotFoundException e) {
            // El driver de PostgreSQL no se encontró en el proyecto
            throw new DatabaseException(
                    "No se encontró el driver de PostgreSQL. Verifique que el .jar esté en el proyecto.", e);

        } catch (SQLException e) {
            // No se pudo conectar a la base de datos
            throw new DatabaseException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    // Método para cerrar la conexión con la base de datos.
    public void cerrar() throws DatabaseException {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al cerrar la conexión: " + e.getMessage(), e);
        }
    }
}
