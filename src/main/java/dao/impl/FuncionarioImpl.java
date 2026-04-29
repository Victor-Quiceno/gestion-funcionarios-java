package dao.impl;

import dao.Conexion;
import dao.FuncionarioDao;
import exception.DatabaseException;
import exception.FuncionarioException;
import model.FuncionarioPOJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioImpl extends Conexion implements FuncionarioDao {

    @Override
    public void ingresar(FuncionarioPOJO func) throws DatabaseException, FuncionarioException {
        // La sentencia SQL con un ? (placeholder) por cada valor que vamos a insertar.
        // Se omite "id_funcionario" porque es SERIAL (se genera solo).
        String query = "INSERT INTO funcionarios (nombres, apellidos, documento, correo, telefono, fecha_ingreso, estado, id_cargo, id_dependencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // El try-with-resources. Java cerrará ambos automáticamente al terminar.
        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query)) {

            // Reemplazamos cada ? con el valor real del POJO (en orden)
            st.setString(1, func.getNombres());
            st.setString(2, func.getApellidos());
            st.setString(3, func.getDocumento());
            st.setString(4, func.getCorreo());
            st.setString(5, func.getTelefono());
            st.setDate(6, func.getFechaIngreso());
            st.setString(7, func.getEstado());
            st.setInt(8, func.getIdCargo());
            st.setInt(9, func.getIdDependencia());

            // Ejecuta el INSERT
            st.executeUpdate();

        } catch (SQLException e) {
            // Si el código de error es "23505", en PostgreSQL significa "unique_violation"
            // (duplicado)
            if ("23505".equals(e.getSQLState())) {
                throw new FuncionarioException("El documento o correo ya se encuentra registrado.", e);
            }
            throw new DatabaseException("Error al registrar el funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificar(FuncionarioPOJO func) throws DatabaseException, FuncionarioException {
        String query = "UPDATE funcionarios SET nombres=?, apellidos=?, documento=?, correo=?, telefono=?, fecha_ingreso=?, estado=?, id_cargo=?, id_dependencia=? WHERE id_funcionario=?";

        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, func.getNombres());
            st.setString(2, func.getApellidos());
            st.setString(3, func.getDocumento());
            st.setString(4, func.getCorreo());
            st.setString(5, func.getTelefono());
            st.setDate(6, func.getFechaIngreso());
            st.setString(7, func.getEstado());
            st.setInt(8, func.getIdCargo());
            st.setInt(9, func.getIdDependencia());

            // El ID va en la posición 10 (para el WHERE)
            st.setInt(10, func.getIdFuncionario());

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new FuncionarioException("No se encontró el funcionario con ID: " + func.getIdFuncionario());
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new FuncionarioException("El nuevo documento o correo ya pertenece a otro funcionario.", e);
            }
            throw new DatabaseException("Error al actualizar el funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int idFuncionario) throws DatabaseException, FuncionarioException {
        String query = "DELETE FROM funcionarios WHERE id_funcionario = ?";

        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, idFuncionario);

            int filasAfectadas = st.executeUpdate();
            if (filasAfectadas == 0) {
                throw new FuncionarioException("No se pudo eliminar: Funcionario no encontrado.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el funcionario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FuncionarioPOJO> listar() throws DatabaseException {
        String query = "SELECT * FROM funcionarios ORDER BY apellidos, nombres";
        List<FuncionarioPOJO> lista = new ArrayList<>();

        // try-with-resources con Connection, PreparedStatement y ResultSet
        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query);
                ResultSet rs = st.executeQuery()) {

            // rs.next() avanza fila por fila en la base de datos
            while (rs.next()) {
                FuncionarioPOJO f = new FuncionarioPOJO();
                f.setIdFuncionario(rs.getInt("id_funcionario"));
                f.setNombres(rs.getString("nombres"));
                f.setApellidos(rs.getString("apellidos"));
                f.setDocumento(rs.getString("documento"));
                f.setCorreo(rs.getString("correo"));
                f.setTelefono(rs.getString("telefono"));
                f.setFechaIngreso(rs.getDate("fecha_ingreso"));
                f.setEstado(rs.getString("estado"));
                f.setIdCargo(rs.getInt("id_cargo"));
                f.setIdDependencia(rs.getInt("id_dependencia"));

                lista.add(f); // Agregamos el POJO lleno a la lista
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los funcionarios: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public FuncionarioPOJO buscarPorId(int idFuncionario) throws DatabaseException, FuncionarioException {
        String query = "SELECT * FROM funcionarios WHERE id_funcionario = ?";
        FuncionarioPOJO f = null;

        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, idFuncionario);

            // El ResultSet se debe cerrar, lo metemos en un try interno
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    f = new FuncionarioPOJO();
                    f.setIdFuncionario(rs.getInt("id_funcionario"));
                    f.setNombres(rs.getString("nombres"));
                    f.setApellidos(rs.getString("apellidos"));
                    f.setDocumento(rs.getString("documento"));
                    f.setCorreo(rs.getString("correo"));
                    f.setTelefono(rs.getString("telefono"));
                    f.setFechaIngreso(rs.getDate("fecha_ingreso"));
                    f.setEstado(rs.getString("estado"));
                    f.setIdCargo(rs.getInt("id_cargo"));
                    f.setIdDependencia(rs.getInt("id_dependencia"));
                } else {
                    throw new FuncionarioException("No se encontró ningún funcionario con ID: " + idFuncionario);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el funcionario: " + e.getMessage(), e);
        }

        return f;
    }
}
