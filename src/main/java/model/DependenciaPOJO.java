package model;

public class DependenciaPOJO {
    private int idDependencia;
    private String nombreDependencia;

    public DependenciaPOJO() {
    }

    public int getIdDependencia() {
        return idDependencia;
    }

    public void setIdDependencia(int idDependencia) {
        this.idDependencia = idDependencia;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    // Útil para los JComboBox de Java Swing
    @Override
    public String toString() {
        return nombreDependencia;
    }
}
