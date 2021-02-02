package Repository;

import Exceptions.RepositoryException;

import java.util.List;

/**
 * Интерфейс основных данных из репозитория.
 *
 * @author Vladislav Tumanov
 */
public interface AnimalRepository {

    /**
     * Метод для получения всех животных из репозитория.
     * @return Список всех животных.
     * @throws RepositoryException если возникли проблемы с доступом.
     */
    List<String[]> getAnimals() throws RepositoryException;
}
