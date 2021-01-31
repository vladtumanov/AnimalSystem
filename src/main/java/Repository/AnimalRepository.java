package Repository;

import java.util.List;

/**
 * Singleton. Класс для доступа к основным данным из репозитория.
 * Для основных данных установлен следущий формат данных:
 * - в качестве разделителя используется знак ';' (точка с запятой);
 * - поиск данных чувствителен к регистру.
 * Пример: (KEY1 or KEY2) and not KEY3.
 *
 * @author Vladislav Tumanov
 */
public class AnimalRepository {

    /** Путь к текстовому файлу репозитория. */
    private final String filePath = "d:/animals.csv";

    /** Общее поле с сылкой на специализированный экземпляр репозитория. */
    private final DataRepository<List<String>> dataRepository = new FileRepository(filePath) { };

    /** Поле для хранения единственного экземпляра данного класса */
    private static AnimalRepository instance;

    /** Запрещено создавать экземпляры данного класса. */
    private AnimalRepository() { }

    /**
     * Метод для получения единственного экземпляра данного класса.
     * Примечание: не работает в многопоточной среде.
     * @return Возвращает единственный экземпляр данного класса
     */
    public static AnimalRepository getInstance() {
        if (instance == null) {
            instance = new AnimalRepository();
        }
        return instance;
    }

    /**
     * Метод для получения всех животных из репозитория.
     * @return Список всех животных.
     */
    public List<String> getAnimals() {
        return dataRepository.findAll();
    }
}
