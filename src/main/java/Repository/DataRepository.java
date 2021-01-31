package Repository;

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
     * @return Найденные данные из репозитория, либо пустой список,
     * если данных нет.
     */
    T findAll();
}
