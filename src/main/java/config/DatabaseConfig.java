package config;

//Configuración centralizada para la conexión a la base de datos PostgreSQL.
public class DatabaseConfig {

    // Driver JDBC de PostgreSQL, necesario para que Java sepa cómo comunicarse con
    // ese motor.
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    // URL de conexión
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/gestion_funcionarios";

    // Credenciales de acceso
    public static final String USER = "";
    public static final String PASSWORD = "Exitoso2026**";
}
