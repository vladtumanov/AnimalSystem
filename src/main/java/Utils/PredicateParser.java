package Utils;

import Exceptions.OperandException;
import Exceptions.OperatorException;
import Exceptions.ParenthesisException;

import java.util.*;
import java.util.function.Predicate;

/**
 * Клас для преобразования сторокого представления правил в объекты {@link Predicate}.
 * <p>При обработке используется обратная польская нотация (далее ОПН, RPN).
 *
 * @author Vladislav Tumanov
 */
public final class PredicateParser {

    /** Временный стек данных для обработки правил */
    private static Deque<String> tempStack;

    /** Список для создания итоговой записи правила в ОПН */
    private static List<String> result;

    /** Запрещается создание экземпляров данного класса */
    private PredicateParser() { }

    /**
     * Метод, преобразующий строкое представление правила в объект {@link Predicate}.
     * Работа данного объекта построена на функии {@link String#contains(CharSequence)}.
     * <p>Для правил установлен следущий формат входных данных:
     * <p>- в качестве разделителя используется пробел;
     * <p>- условия указываются совместно с номером колонки, к которым
     * они отностяся через знак равенства (напр. 1=KEY);
     * <p>- нумерация колонок начинается слева с единицы;
     * <p>- применяются только операторы and, or, not;
     * <p>- допускается использование круглых скобок;
     * <p>- операторы чувствительны к регистру (напр. or != OR).
     * <p>Пример: (1=KEY1 or 2=KEY2) and not 3=KEY3.
     * @param rule Строкое представление правла.
     * @return {@link Predicate} представляющий правило.
     * @throws ParenthesisException если в правиле неверно расставлены скобки.
     * @throws OperatorException если в правиле неверно указаны операторы.
     * @throws OperandException если в правиле неверно указан операнд.
     */
    public static synchronized Predicate<String[]> parse(String rule) throws ParenthesisException,
            OperatorException, OperandException {
        return doParse(toRPN(rule));
    }

    /**
     * Метод, преобразующий строкое представление правила в ОПН.
     * @param rule Строкое представление правла.
     * @return Запись ОПН как списка.
     * @throws ParenthesisException если в правиле неверно расставлены скобки.
     * @throws OperatorException если в правиле неверно указаны операторы.
     */
    private static List<String> toRPN(String rule) throws ParenthesisException, OperatorException {
        tempStack = new ArrayDeque<>();
        result = new ArrayList<>();
        String[] rulesArray = ruleToArray(rule);
        checkRule(rulesArray);

        for (var element : rulesArray) {
            switch (element) {
                case "or", "and", "not" -> gotOperator(element);
                case "(" -> tempStack.push(element);
                case ")" -> gotParen();
                case "" -> {} //skip empty
                default -> result.add(element);
            }
        }
        while (!tempStack.isEmpty()) {
            result.add(tempStack.pop());
        }
        return result;
    }

    /**
     * Метод, обрабатывающий операторы в выражении и сортирующий его в соостветствии с ОПН.
     * @param operator Оператор правила.
     */
    private static void gotOperator(String operator) {
        while (!tempStack.isEmpty()) {
            String opTop = tempStack.pop();
            if (opTop.equals("(")) {
                tempStack.push(opTop);
                break;
            } else {
                if (getPriority(opTop) <= getPriority(operator)) {
                    tempStack.push(opTop);
                    break;
                } else
                    result.add(opTop);
            }
        }
        tempStack.push(operator);
    }

    /**
     * Метод сортирующий операнды, когда в правиле закрывается скобка.
     */
    private static void gotParen() {
        while (!tempStack.isEmpty()) {
            String element = tempStack.pop();
            if (element.equals("("))
                break;
            else
                result.add(element);
        }
    }

    /**
     * Перевод строкового представления правила в массив элементов этого правила.
     * @param rule Строкое представление правла.
     * @return Массив, состоящий из элементов правила.
     */
    private static String[] ruleToArray(String rule) {
        return rule
                .replace("(", " ( ")
                .replace(")", " ) ")
                .split("\\s+");
    }

    /**
     * Проверка корректности правила.
     * @param rulesArray Правило, в виде массива элементов.
     * @throws ParenthesisException если в правиле неверно расставлены скобки.
     * @throws OperatorException если в правиле неверно указаны операторы.
     */
    private static void checkRule(String[] rulesArray) throws ParenthesisException, OperatorException {
        int operatorCount = 0;
        int operandCount = 0;
        int parenthesisSum = 0;
        boolean previousAndOr = false;

        for (var element : rulesArray) {
            switch (element) {
                case "or", "and" -> {
                    if (previousAndOr)
                        throw new OperatorException("Doubled operator");
                    previousAndOr = true;
                    operatorCount++;
                }
                case "(" -> parenthesisSum++;
                case ")" -> {
                    if (--parenthesisSum < 0)
                        throw new ParenthesisException("Open parentheses not found");
                }
                case "not" -> {
                    if (operandCount > 0 && !previousAndOr)
                        throw new OperatorException("'not' operator error");
                    previousAndOr = false;
                }
                case "" -> {} //skip
                default -> {
                    operandCount++;
                    previousAndOr = false;
                }
            }
        }
        if (parenthesisSum > 0)
            throw new ParenthesisException("Close parentheses not found");

        if (operatorCount != operandCount - 1)
            throw new OperatorException("Error operator count");
    }

    /**
     * Определение приоритета оператора.
     * @param operator оператор.
     * @return приоритет (больше - выше).
     */
    private static int getPriority(String operator) {
        return switch (operator) {
            case "or" -> 1;
            case "and" -> 2;
            case "not" -> 3;
            default -> 0;
        };
    }

    /**
     * Преобразование ОПН в объект {@link Predicate}.
     * @param rpn ОПН как список элементов.
     * @return {@link Predicate} представляющий правило.
     */
    private static Predicate<String[]> doParse(List<String> rpn) {
        Deque<Predicate<String[]>> merged = new ArrayDeque<>();
        Predicate<String[]> pred1, pred2;

        // element format 1="KEY"
        for (var element : rpn) {
            if (element.equals("not")) {
                pred2 = merged.pop();
                merged.push(pred2.negate());
            } else if (element.equals("and") || element.equals("or")) {
                pred2 = merged.pop();
                pred1 = merged.pop();
                merged.push(element.equals("or") ? pred1.or(pred2) : pred1.and(pred2));
            } else {
                try {
                    String[] columnRule = element.split("=");
                    int column = Integer.parseInt(columnRule[0]);
                    // Note: in String[] column index with 0, but in rule with 1.
                    merged.push(p -> p[column - 1].equalsIgnoreCase(columnRule[1])); // <-- Create predicate is here
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    throw new OperandException(e.getMessage());
                }
            }
        }
        return merged.pop();
    }
}