package Repository;

import Exceptions.RepositoryException;

import java.util.List;

/**
 * Интерфейс для получения списка правил из репозитория.
 *
 * @author Vladislav Tumanov
 */
public interface RulesRepository {

    /**
     * Метод для получения правил из репозитория.
     * @return Список правил.
     * @throws RepositoryException если возникли проблемы с доступом.
     */
    List<String> getRules() throws RepositoryException;
}
