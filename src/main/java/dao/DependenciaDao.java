package dao;

import exception.DatabaseException;
import model.DependenciaPOJO;
import java.util.List;

public interface DependenciaDao {
    public List<DependenciaPOJO> listar() throws DatabaseException;
}
