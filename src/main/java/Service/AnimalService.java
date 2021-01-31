package Service;

import Exceptions.OperatorException;
import Exceptions.ParenthesisException;
import Repository.AnimalRepository;
import Repository.RulesRepository;
import Utils.PredicateParser;
import View.ResultView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Класс, содержащий основную логику работы приложения.
 *
 * @author Vladislav Tumanov
 */
public class AnimalService {

    /** Поле с интерфейсом для вывода данных */
    ResultView view;

    /** Репозиторий основных данных */
    AnimalRepository animalRepository;

    /** Репозиторий правил */
    RulesRepository rulesRepository;

    /**
     * Инициализация объекта для корректной работы логики приложения.
     * @param view Объект, куда выводить результат вычислений.
     */
    public AnimalService(ResultView view) {
        this.view = view;
        animalRepository = AnimalRepository.getInstance();
        rulesRepository = RulesRepository.getInstance();
    }

    /**
     * Метод запускающий обаботку входных данных с выводом результата в {@link AnimalService#view}.
     */
    public void run() {
        List<String> animals = animalRepository.getAnimals();
        getPredicates().stream()
                .mapToLong(pred -> animals.stream().filter(pred).count())
                .forEach(view::show);
    }

    /**
     * Преобразование списка правил из репозитория в объект {@link Predicate}.
     * @return Список {@link Predicate}, созданных из правил.
     */
    private List<Predicate<String>> getPredicates() {
        List<Predicate<String>> predicates = new ArrayList<>();

        for (var rule : rulesRepository.getRules()) {
            try {
                predicates.add(PredicateParser.parse(rule));
            } catch (ParenthesisException | OperatorException pex) {
                System.err.printf("%s [%s]%n", pex.getMessage(), rule);
            }
        }
        return predicates;
    }
}
