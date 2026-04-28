package dao.impl;

import dao.Conexion;
import dao.FuncionarioDao;
import model.FuncionarioPOJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioImpl extends Conexion implements FuncionarioDao {
    @Override
    public void ingresar(FuncionarioPOJO func) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.conexion.prepareStatement("INSERT INTO Funcionario(nombre) VALUES (?)");
            st.setString(1, func.getNombres());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrar();
        }
    }

    @Override
    public void modificar(FuncionarioPOJO func) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.conexion.prepareStatement("UPDATE Funcionario SET nombre=? WHERE id=?");
            st.setString(1, func.getNombres());
            st.setInt(2,func.getId());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrar();
        }

    }

    @Override
    public void eliminar(FuncionarioPOJO func) throws Exception {
        try {
            this.conectar();
            PreparedStatement st = this.conexion.prepareStatement("DELETE FROM  Funcionario WHERE id = ?");
            st.setInt(1, func.getId());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.cerrar();
        }

    }

    @Override
    public List<FuncionarioPOJO> listar() {
        String query = "SELECT * FROM funcionario";
        List<FuncionarioPOJO> lista = new ArrayList<>();

        try (Connection con = this.conectar();
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery()) {

            while (rs.next()){
                FuncionarioPOJO f = new FuncionarioPOJO();
                f.setId(rs.getInt("id"));
                f.setNombres(rs.getString("nombres"));
                lista.add(f);
            }


        } catch (SQLException e) {
            throw new Exception("Error al consultar funcionarios" + e.getMessage());
        }
        return lista;
    }
}
