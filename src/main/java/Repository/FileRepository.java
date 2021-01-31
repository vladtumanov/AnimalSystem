package Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Абстрактный класс репозитория с доступом к текстовым файлам.
 * Примечание: для корректной работы, текст в файле должен быть
 * в UTF-8 кодировке.
 *
 * @author Vladislav Tumanov
 */
public abstract class FileRepository implements DataRepository<List<String>> {

    /** Полне с информацией о пути к файлу. */
    private final Path path;

    /**
     * Конструктор для создания экземпляра файлового репозитория.
     * @param filePath Путь к текстовому файлу.
     */
    public FileRepository(String filePath) {
        this.path = Path.of(filePath);
    }

    /**
     * Метод, возвращающий все данные из репозитория. Если произошла ошибка доступа
     * к файлу репозитория, то в стандартный поток вывода ошибок отправляется сообщение
     * с описанием ошибки.
     * @return Список строк из репозитория. Если данных нет, то возвращается пустой список.
     * @see DataRepository#findAll()
     */
    @Override
    public final List<String> findAll() {
        try {
            return Files.lines(path, StandardCharsets.UTF_8)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return List.of();
        }
    }
}
