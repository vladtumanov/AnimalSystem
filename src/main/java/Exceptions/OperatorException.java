package Exceptions;

/**
 * Сигнализирует о том, что произошло какое-либо исключение, связанное с операторами правила.
 *
 * @author Vladislav Tumanov
 */
public class OperatorException extends Exception {

    /**
     * Создаёт исключение {@code OperatorException} с указанным подробным сообщением.
     * @param message Сообщение возникшего исключения.
     */
    public OperatorException(String message) {
        super(message);
    }
}
