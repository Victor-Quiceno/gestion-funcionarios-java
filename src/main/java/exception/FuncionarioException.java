package exception;

//Excepción de funcionario, se usa para cuando la lógica de este falla
public class FuncionarioException extends Exception {

    // Constructor con solo el mensaje
    public FuncionarioException(String mensaje) {
        super(mensaje);
    }

    // Constructor con mensaje y causa
    public FuncionarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
