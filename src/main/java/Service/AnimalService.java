package Service;

import Exceptions.OperatorException;
import Exceptions.ParenthesisException;
import Exceptions.RepositoryException;
import Repository.AnimalRepository;
import Repository.RulesRepository;
import Repository.RulesRepositoryImpl;
import Utils.PredicateParser;
import View.ResultView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Класс, содержащий основную логику работы приложения.
 *
 * @author Vladislav Tumanov
 */
public class AnimalService {

    /** Поле с интерфейсом для вывода данных */
    private final ResultView view;

    /** Репозиторий основных данных */
    private final AnimalRepository animalRepository;

    /** Репозиторий правил */
    private final RulesRepository rulesRepository;

    /**
     * Инициализация объекта для корректной работы логики приложения.
     * @param view Объект, куда выводить результат вычислений.
     * @param animalRepository Репозиторий обновных данных.
     * @param rulesRepository Репозиторий правил.
     */
    public AnimalService(ResultView view, AnimalRepository animalRepository, RulesRepository rulesRepository) {
        this.view = view;
        this.animalRepository = animalRepository;
        this.rulesRepository = rulesRepository;
    }

    /**
     * Метод запускающий обаботку входных данных с выводом результата в {@link AnimalService#view}.
     * @throws RepositoryException если возникли проблемы с доступом.
     * @throws OperatorException если в правиле неверно указаны операторы.
     * @throws ParenthesisException если в правиле неверно расставлены скобки.
     */
    public void run() throws RepositoryException, OperatorException, ParenthesisException {
        List<String> animals = animalRepository.getAnimals();
        getPredicates().stream()
                .mapToLong(pred -> animals.stream().filter(pred).count())
                .forEach(view::show);
    }

    /**
     * Преобразование списка правил из репозитория в объект {@link Predicate}.
     * @return Список {@link Predicate}, созданных из правил.
     * @throws RepositoryException если возникли проблемы с доступом.
     * @throws OperatorException если в правиле неверно указаны операторы.
     * @throws ParenthesisException если в правиле неверно расставлены скобки.
     */
    private List<Predicate<String>> getPredicates() throws RepositoryException, OperatorException,
            ParenthesisException {
        List<Predicate<String>> predicates = new ArrayList<>();

        for (var rule : rulesRepository.getRules())
            predicates.add(PredicateParser.parse(rule));

        return predicates;
    }
}
