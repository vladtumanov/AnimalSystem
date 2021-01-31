package Repository;

import java.util.List;

/**
 * Singleton. Класс для доступа к правилам из репозитория.
 * Для правил установлен следущий формат данных:
 * - в качестве разделителя используется пробел;
 * - применятся только операторы and, or, not;
 * - допускается использование круглых скобок;
 * - плавила чувствительны к регистру (напр. or != OR).
 * Пример: (KEY1 or KEY2) and not KEY3.
 *
 * @author Vladislav Tumanov
 */
public class RulesRepository {

    /** Путь к текстовому файлу репозитория. */
    private final String filePath = "d:/rules.txt";

    /** Общее поле с сылкой на специализированный экземпляр репозитория. */
    private final DataRepository<List<String>> dataRepository = new FileRepository(filePath) { };

    /** Поле для хранения единственного экземпляра данного класса */
    private static RulesRepository instance;

    /** Запрещено создавать экземпляры данного класса. */
    private RulesRepository() { }

    /**
     * Метод для получения единственного экземпляра данного класса.
     * Примечание: не работает в многопоточной среде.
     * @return Возвращает единственный экземпляр данного класса
     */
    public static RulesRepository getInstance() {
        if (instance == null) {
            instance = new RulesRepository();
        }
        return instance;
    }

    /**
     * Метод для получения правил из репозитория.
     * @return Список правил.
     */
    public List<String> getRules() {
        return dataRepository.findAll();
    }
}
