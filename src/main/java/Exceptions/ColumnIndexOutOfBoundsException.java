package Exceptions;

/**
 * Сигнализирует о том, что произошло какое-либо исключение, связанное с
 * неверным указанием индекса колонки.
 *
 * @author Vladislav Tumanov
 */
public class ColumnIndexOutOfBoundsException extends RuntimeException {

    /**
     * Создаёт исключение {@code ColumnIndexOutOfBoundsException} с указанным подробным сообщением.
     * @param message Сообщение возникшего исключения.
     */
    public ColumnIndexOutOfBoundsException(String message) {
        super(message);
    }
}
