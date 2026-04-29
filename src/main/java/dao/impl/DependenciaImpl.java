package dao.impl;

import dao.Conexion;
import dao.DependenciaDao;
import exception.DatabaseException;
import model.DependenciaPOJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DependenciaImpl extends Conexion implements DependenciaDao {

    @Override
    public List<DependenciaPOJO> listar() throws DatabaseException {
        String query = "SELECT * FROM dependencias ORDER BY nombre_dependencia";
        List<DependenciaPOJO> lista = new ArrayList<>();

        try (Connection con = this.conectar();
                PreparedStatement st = con.prepareStatement(query);
                ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                DependenciaPOJO dep = new DependenciaPOJO();
                dep.setIdDependencia(rs.getInt("id_dependencia"));
                dep.setNombreDependencia(rs.getString("nombre_dependencia"));
                lista.add(dep);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al consultar las dependencias: " + e.getMessage(), e);
        }

        return lista;
    }
}
