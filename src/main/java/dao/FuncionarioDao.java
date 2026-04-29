package dao;

import exception.DatabaseException;
import exception.FuncionarioException;
import model.FuncionarioPOJO;

import java.util.List;

// Interfaz que define el contrato del CRUD para la entidad Funcionario.
public interface FuncionarioDao {

    // Crear un nuevo funcionario
    public void ingresar(FuncionarioPOJO func) throws DatabaseException, FuncionarioException;

    // Actualizar los datos de un funcionario existente
    public void modificar(FuncionarioPOJO func) throws DatabaseException, FuncionarioException;

    // Eliminar un funcionario por su ID
    public void eliminar(int idFuncionario) throws DatabaseException, FuncionarioException;

    // Listar todos los funcionarios
    public List<FuncionarioPOJO> listar() throws DatabaseException;

    // Buscar un funcionario específico por su ID
    public FuncionarioPOJO buscarPorId(int idFuncionario) throws DatabaseException, FuncionarioException;
}
