package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //Datos de conexión
    protected Connection conexion;
    private final String JDBC_DRIVER;
    private final String DB_URL;

    //Credenciales de acceso
    private final String USER;
    private final String PASSWORD;

    public void conectar() throws Exception {
        try {

            conexion = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Class.forName(JDBC_DRIVER);

        } catch (Exception e) {
            throw e;
        }
    }

    public void cerrar() throws SQLException{
        if (conexion != null && !conexion.isClosed()){
            conexion.close();
        }
    }


}
