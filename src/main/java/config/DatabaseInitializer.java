package config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    /**
     * Verifica si la base de datos existe. Si no, la crea y ejecuta los scripts de inicialización.
     * @throws Exception Si no hay conexión al motor de PostgreSQL o hay fallos de SQL.
     */
    public static void init() throws Exception {
        // Forzar carga del driver
        Class.forName(DatabaseConfig.JDBC_DRIVER);

        String fullUrl = DatabaseConfig.DB_URL;
        int lastSlash = fullUrl.lastIndexOf('/');
        String baseUrl = fullUrl.substring(0, lastSlash); // ej: jdbc:postgresql://localhost:5432
        String dbName = fullUrl.substring(lastSlash + 1); // ej: gestion_funcionarios

        // 1. Conectarnos a la base de datos administrativa 'postgres' para revisar si la nuestra existe
        String adminUrl = baseUrl + "/postgres";
        
        try (Connection adminConn = DriverManager.getConnection(adminUrl, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = adminConn.createStatement()) {

            // Buscar si la base de datos ya existe
            String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
            try (ResultSet rs = stmt.executeQuery(checkDbQuery)) {
                if (!rs.next()) {
                    System.out.println("La base de datos '" + dbName + "' no existe. Creándola automáticamente...");
                    // Crear base de datos
                    stmt.executeUpdate("CREATE DATABASE " + dbName);
                    System.out.println("Base de datos creada exitosamente.");
                    
                    // Solo si acabamos de crear la base de datos, corremos los scripts de tablas e inserts
                    ejecutarScripts(fullUrl, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
                } else {
                    System.out.println("La base de datos '" + dbName + "' ya existe. No se requiere inicialización.");
                }
            }
        }
    }

    private static void ejecutarScripts(String url, String user, String password) throws Exception {
        try (Connection targetConn = DriverManager.getConnection(url, user, password);
             Statement stmt = targetConn.createStatement()) {

            System.out.println("Ejecutando script de creación de tablas...");
            ejecutarArchivoSql(stmt, "/database/script-create.sql");

            System.out.println("Ejecutando script de inserción de datos...");
            ejecutarArchivoSql(stmt, "/database/script-insert.sql");
            
            System.out.println("Inicialización de la base de datos completada.");
        }
    }

    private static void ejecutarArchivoSql(Statement stmt, String resourcePath) throws Exception {
        // Leer el archivo desde los resources de Maven
        InputStream is = DatabaseInitializer.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new Exception("No se encontró el archivo SQL en los resources: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String sqlContent = reader.lines().collect(Collectors.joining("\n"));
            
            // Separar los comandos por punto y coma (;)
            String[] comandos = sqlContent.split(";");
            
            for (String comando : comandos) {
                if (comando.trim().isEmpty()) {
                    continue;
                }
                stmt.execute(comando.trim());
            }
        }
    }
}
