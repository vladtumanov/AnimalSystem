package Repository;

import Exceptions.RepositoryException;

import java.util.Collection;

/**
 * Общий интерфейс репозитория.
 * @param <T> Возвращаемый из репозитория тип данных.
 *
 * @author Vladislav Tumanov
 */
public interface DataRepository<T extends Collection<?>> {

    /**
     * Метод, возвращающий все данные из репозитория.
     * @return Найденные данные из репозитория, либо пустой список, если данных нет.
     * @throws RepositoryException если возникли проблемы с доступом.
     */
    T findAll() throws RepositoryException;
}
