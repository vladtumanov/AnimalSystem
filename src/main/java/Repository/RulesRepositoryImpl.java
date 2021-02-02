package Repository;

import Exceptions.RepositoryException;

import java.util.List;

/**
 * Singleton. Класс для доступа к правилам из репозитория.
 * <p>Для правил установлен следущий формат данных:
 * <p>- в качестве разделителя используется пробел;
 * <p>- условия указываются совместно с номером колонки, к которым
 * они отностяся через знак равенства (напр. 1=KEY);
 * <p>- нумерация колонок начинается слева с единицы;
 * <p>- применятся только операторы and, or, not;
 * <p>- допускается использование круглых скобок;
 * <p>- поиск данных нечувствителен к регистру.
 * <p>- операторы чувствительны к регистру (напр. or != OR).
 * <p>Пример: (1=KEY1 or 2=KEY2) and not 3=KEY3.
 *
 * @author Vladislav Tumanov
 */
public class RulesRepositoryImpl implements RulesRepository {

    /** Путь к текстовому файлу репозитория. */
    private final String filePath = "d:/rules.txt";

    /** Общее поле с сылкой на специализированный экземпляр репозитория. */
    private final DataRepository<List<String>> dataRepository = new FileRepository(filePath) { };

    /** Поле для хранения единственного экземпляра данного класса */
    private static RulesRepositoryImpl instance;

    /** Запрещено создавать больше одного экземпляра данного класса. */
    private RulesRepositoryImpl() { }

    /**
     * Метод для получения единственного экземпляра данного класса.
     * <p>Примечание: не работает в многопоточной среде.
     * @return Возвращает единственный экземпляр данного класса
     */
    public static RulesRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RulesRepositoryImpl();
        }
        return instance;
    }

    /**
     * Метод для получения правил из репозитория.
     * @return Список правил.
     * @throws RepositoryException если возникли проблемы с доступом.
     */
    @Override
    public List<String> getRules() throws RepositoryException {
        return dataRepository.findAll();
    }
}
