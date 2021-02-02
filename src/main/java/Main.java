import Repository.AnimalRepositoryImpl;
import Repository.RulesRepositoryImpl;
import Service.AnimalService;
import View.ResultView;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Vladislav Tumanov
 */
public class Main {

    private static final Logger logger = Logger.getLogger(AnimalService.class.getName());

    /**
     * Точка входа. Инициализация сервисного класса.
     * @param args аргументы запуска.
     */
    public static void main(String[] args) {

        AtomicInteger number = new AtomicInteger();

        ResultView view = resultCount -> System.out.printf("Result rules #%d: %d%n",
                number.incrementAndGet(), resultCount);

        AnimalService animalService = new AnimalService(view,
                AnimalRepositoryImpl.getInstance(),
                RulesRepositoryImpl.getInstance());

        try {
            animalService.run();
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
