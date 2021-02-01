package Exceptions;

/**
 * Сигнализирует о том, что произошло какое-либо исключение, связанное с доступом к репозиторию.
 *
 * @author Vladislav Tumanov
 */
public class RepositoryException extends Exception {

    /**
     * Создаёт исключение {@code RepositoryException} с указанным подробным сообщением.
     * @param message Сообщение возникшего исключения.
     */
    public RepositoryException(String message) {
        super(message);
    }
}
