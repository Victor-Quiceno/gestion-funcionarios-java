package dao;

import exception.DatabaseException;
import model.CargoPOJO;
import java.util.List;

public interface CargoDao {
    public List<CargoPOJO> listar() throws DatabaseException;
}
