package Exceptions;

/**
 * Сигнализирует о том, что произошло какое-либо исключение,
 * связанное с разбором скобок в правиле.
 *
 * @author Vladislav Tumanov
 */
public class ParenthesisException extends Exception {

    /**
     * Создаёт исключение {@code ParenthesisException} с указанным подробным сообщением.
     * @param message Сообщение возникшего исключения.
     */
    public ParenthesisException(String message) {
        super(message);
    }
}
