package Service;

import Exceptions.ColumnIndexOutOfBoundsException;
import Exceptions.OperatorException;
import Exceptions.ParenthesisException;
import Repository.AnimalRepository;
import Repository.RulesRepository;
import View.ResultView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

class AnimalServiceTest {

    private static List<String[]> animals;

    private AnimalRepository animalRepository;

    private RulesRepository rulesRepository;

    private ResultView view;

    private AnimalService animalService;

    @BeforeAll
    static void beforeAll() {
        animals = of(
                new String[]{"ЛЕГКОЕ", "МАЛЕНЬКОЕ", "ВСЕЯДНОЕ"},
                new String[]{"ТЯЖЕЛОЕ", "МАЛЕНЬКОЕ", "ТРАВОЯДНОЕ"},
                new String[]{"ТЯЖЕЛОЕ", "НЕВЫСОКОЕ", "ТРАВОЯДНОЕ"});
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
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ТРАВОЯДНОЕ",
                "(3=ТРАВОЯДНОЕ or 3=ПЛОТОЯДНОЕ) and 2=МАЛЕНЬКОЕ", "3=ВСЕЯДНОЕ and not 2=ВЫСОКОЕ")));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view, Mockito.times(1)).show(2L);
        Mockito.verify(view, Mockito.times(2)).show(1L);
    }

    @Test
    void exceptionOperators() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ТРАВОЯДНОЕ not")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ВСЕЯДНОЕ not and 3=ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ВСЕЯДНОЕ or and 3=ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("or 3=ВСЕЯДНОЕ and 3=ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ВСЕЯДНОЕ 3=ТРАВОЯДНОЕ")));
        assertThrows(OperatorException.class, ()-> animalService.run());
    }

    @Test
    void exceptionParenthesis() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of(")3=ТРАВОЯДНОЕ")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("3=ТРАВОЯДНОЕ (")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of(")3=ТРАВОЯДНОЕ(")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());

        assertDoesNotThrow(() -> when(rulesRepository.getRules())
                .thenReturn(of("(3=ТРАВОЯДНОЕ or 3=ПЛОТОЯДНОЕ) and 3=МАЛЕНЬКОЕ)")));
        assertThrows(ParenthesisException.class, ()-> animalService.run());
    }

    @Test
    void exceptionColumnIndex() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("4=ТРАВОЯДНОЕ")));
        assertThrows(ColumnIndexOutOfBoundsException.class, () -> animalService.run());
    }

    @Test
    void withoutAnimals() {
        assertDoesNotThrow(() -> when(animalRepository.getAnimals()).thenReturn(List.<String[]>of(new String[] {""})));
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of("1=ТРАВОЯДНОЕ")));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view).show(0L);
    }

    @Test
    void withoutRules() {
        assertDoesNotThrow(() -> when(rulesRepository.getRules()).thenReturn(of()));
        assertDoesNotThrow(() -> animalService.run());
        Mockito.verify(view, Mockito.never()).show(Mockito.anyLong());
    }
}