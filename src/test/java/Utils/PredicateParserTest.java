package Utils;

import Exceptions.OperatorException;
import Exceptions.ParenthesisException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class PredicateParserTest {

    @Test
    void emptyRuleException() {
        assertThrows(OperatorException.class, () -> PredicateParser.parse(""));
    }

    @Test
    void notOperatorException() {
        assertThrows(OperatorException.class, () -> PredicateParser.parse("not"));
        assertThrows(OperatorException.class, () -> PredicateParser.parse("KEY not"));
        assertThrows(OperatorException.class, () -> PredicateParser.parse("not and KEY"));
    }

    @Test
    void parenthesisException() {
        assertThrows(ParenthesisException.class, () -> PredicateParser.parse("("));
        assertThrows(ParenthesisException.class, () -> PredicateParser.parse("(KEY"));
        assertThrows(ParenthesisException.class, () -> PredicateParser.parse(")KEY( and (KEY1 or KEY2)"));
        assertThrows(ParenthesisException.class, () -> PredicateParser.parse("(KEY and (KEY and KEY)"));
        assertThrows(ParenthesisException.class, () -> PredicateParser.parse("(KEY and KEY2) or not KEY3)"));
    }

    @Test
    void correctPredicates() {
        String key1 = "foo";
        String key2 = "bar";

        AtomicReference<Predicate<String>> actual = new AtomicReference<>();

        assertDoesNotThrow(() -> actual.set(PredicateParser.parse(key1)));
        assertTrue(actual.get().test(key1));

        assertDoesNotThrow(() -> actual.set(PredicateParser.parse("not " + key1)));
        assertFalse(actual.get().test(key1));

        assertDoesNotThrow(() -> actual.set(PredicateParser.parse(key1 + " or " + key2)));
        assertTrue(actual.get().test(key1));
        assertTrue(actual.get().test(key2));

        assertDoesNotThrow(() -> actual.set(PredicateParser.parse(key1 + " and " + key2)));
        assertFalse(actual.get().test(key1));
        assertFalse(actual.get().test(key2));
    }
}