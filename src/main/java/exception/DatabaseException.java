package exception;

//Excepción personalizada para errores relacionados con la base de datos.

public class DatabaseException extends Exception {

    //Constructor con un solo mensaje para describir el error sin adjuntar la causa original.
    public DatabaseException(String mensaje) {
        super(mensaje);
    }

    //Constructor con mensaje y causa para envolver la excepción original.
    public DatabaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
