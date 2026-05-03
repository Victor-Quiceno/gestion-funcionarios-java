package config;

import io.github.cdimascio.dotenv.Dotenv;

//Configuración centralizada para la conexión a la base de datos PostgreSQL.
public class DatabaseConfig {
    // Carga las variables desde el archivo .env de forma segura
    private static final Dotenv dotenv = Dotenv.load();

    // El driver sigue siendo estático porque no es un dato sensible
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    // Las credenciales se leen desde la memoria, nunca se suben a GitHub
    public static final String DB_URL = dotenv.get("DB_URL");
    public static final String USER = dotenv.get("DB_USER");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");
}
