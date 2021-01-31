package View;

/**
 * Общий интерфейс для вывода результатов.
 *
 * @author Vladislav Tumanov
 */
public interface ResultView {

    /**
     * Метод отображающий результат применения правил.
     * @param count Количество данных, соотвсетсвующих правилу.
     */
    void show(long count);
}
