package dao;

import model.FuncionarioPOJO;

import java.util.List;

public interface FuncionarioDao {
    public void ingresar (FuncionarioPOJO func) throws Exception;
    public void modificar (FuncionarioPOJO func) throws Exception;
    public void eliminar (FuncionarioPOJO func) throws Exception;
    public List<FuncionarioPOJO> listar();
}
