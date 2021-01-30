package Service;

import Repository.AnimalRepository;
import Repository.RulesRepository;
import Utils.PredicateParser;
import View.ResultView;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AnimalService {

    ResultView view;

    public AnimalService(ResultView view) {
        this.view = view;
    }

    public void run() {
        List<String> animals = AnimalRepository.getAnimals();
        getPredicates().stream()
                .mapToLong(pred -> animals.stream().filter(pred).count())
                .forEach(view::show);
    }

    private List<Predicate<String>> getPredicates() {
        return RulesRepository.getRules().stream()
                .map(PredicateParser::parse)
                .collect(Collectors.toList());
    }
}
