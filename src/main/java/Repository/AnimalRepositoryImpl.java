package Repository;

import Exceptions.RepositoryException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton. Класс для доступа к основным данным из репозитория.
 * <p>Для основных данных установлен следущий формат данных:
 * <p>- в качестве разделителя используется знак ';' (точка с запятой);
 * <p>Пример: Свойство1;Свойство2;Свойство3;...
 *
 * @author Vladislav Tumanov
 */
public class AnimalRepositoryImpl implements AnimalRepository {

    /** Путь к текстовому файлу репозитория. */
    private final String filePath = "d:/animals.csv";

    /** Общее поле с сылкой на специализированный экземпляр репозитория. */
    private final DataRepository<List<String>> dataRepository = new FileRepository(filePath) { };

    /** Поле для хранения единственного экземпляра данного класса */
    private static AnimalRepositoryImpl instance;

    /** Запрещено создавать больше одного экземпляра данного класса. */
    private AnimalRepositoryImpl() { }

    /**
     * Метод для получения единственного экземпляра данного класса.
     * <p>Примечание: не работает в многопоточной среде.
     * @return Возвращает единственный экземпляр данного класса
     */
    public static AnimalRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new AnimalRepositoryImpl();
        }
        return instance;
    }

    /**
     * Метод для получения всех животных из репозитория.
     * @return Список всех животных.
     * @throws RepositoryException если возникли проблемы с чтением данных.
     */
    @Override
    public List<String[]> getAnimals() throws RepositoryException {
        return dataRepository.findAll().stream()
                .map(animal -> animal.split(";"))
                .collect(Collectors.toList());
    }
}
