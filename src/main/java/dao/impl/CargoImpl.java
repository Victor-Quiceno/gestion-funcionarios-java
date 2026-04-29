package dao.impl;

import dao.CargoDao;
import dao.Conexion;
import exception.DatabaseException;
import model.CargoPOJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoImpl extends Conexion implements CargoDao {

    @Override
    public List<CargoPOJO> listar() throws DatabaseException {
        String query = "SELECT * FROM cargos ORDER BY nombre_cargo";
        List<CargoPOJO> lista = new ArrayList<>();

        try (Connection con = this.conectar();
             PreparedStatement st = con.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                CargoPOJO cargo = new CargoPOJO();
                cargo.setIdCargo(rs.getInt("id_cargo"));
                cargo.setNombreCargo(rs.getString("nombre_cargo"));
                lista.add(cargo);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error al consultar los cargos: " + e.getMessage(), e);
        }

        return lista;
    }
}
