package Service;

import Exceptions.OperatorException;
import Exceptions.ParenthesisException;
import Repository.AnimalRepository;
import Repository.RulesRepository;
import View.ResultView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

class AnimalServiceTest {

    private static List<String> animals;

    private AnimalRepository animalRepository;

    private RulesRepository rulesRepository;

    private ResultView view;

    private AnimalService animalService;

    @BeforeAll
    static void beforeAll() {
        animals = List.of(
                "ЛЕГКОЕ;МАЛЕНЬКОЕ;ВСЕЯДНОЕ",
                "ТЯЖЕЛОЕ;МАЛЕНЬКОЕ;ТРАВОЯДНОЕ",
                "ТЯЖЕЛОЕ;НЕВЫСОКОЕ;ТРАВОЯДНОЕ");
    }

    @BeforeEach
    void setUp() {
        view = Mockito.mock(ResultView.class);
        animalRepository = Mockito.mock(AnimalRepository.class);
        rulesRepository = Mockito.mock(RulesRepository.class);
        animalService = new AnimalService(view, animalRepository, rulesRepository);
        assertDoesNotThrow(() -> when(animalRepository.getAnimals()).thenReturn(animals));
    }

    @Test
    void correctResult() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ТРАВОЯДНОЕ",
                "(ТРАВОЯДНОЕ or ПЛОТОЯДНОЕ) and МАЛЕНЬКОЕ", "ВСЕЯДНОЕ and not ВЫСОКОЕ")));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view, Mockito.times(1)).show(2L);
        Mockito.verify(view, Mockito.times(2)).show(1L);
    }

    @Test
    void exceptionOperators() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ТРАВОЯДНОЕ not")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ВСЕЯДНОЕ not and ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ВСЕЯДНОЕ or and ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("or ВСЕЯДНОЕ and ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ВСЕЯДНОЕ ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());
    }

    @Test
    void exceptionParenthesis() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of(")ТРАВОЯДНОЕ")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ТРАВОЯДНОЕ (")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of(")ТРАВОЯДНОЕ(")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules())
                .thenReturn(List.of("(ТРАВОЯДНОЕ or ПЛОТОЯДНОЕ) and МАЛЕНЬКОЕ)")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());
    }

    @Test
    void withoutAnimals() {
        assertDoesNotThrow(() -> when(animalRepository.getAnimals()).thenReturn(List.of("")));
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of("ТРАВОЯДНОЕ")));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view).show(0L);
    }

    @Test
    void withoutRules() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(List.of()));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view, Mockito.never()).show(Mockito.anyLong());
    }
}