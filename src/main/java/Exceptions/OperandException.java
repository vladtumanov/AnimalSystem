package Exceptions;

/**
 * Сигнализирует о том, что произошло какое-либо исключение, связанное с операндом правила.
 *
 * @author Vladislav Tumanov
 */
public class OperandException extends RuntimeException {

    /**
     * Создаёт исключение {@code OperandException} с указанным подробным сообщением.
     * @param message Сообщение возникшего исключения.
     */
    public OperandException(String message) {
        super(message);
    }
}
